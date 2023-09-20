<template>
  <div style="width: 100%;height: 100vh;background-color: #2f36be;overflow: hidden">
    <div style="width: 400px;margin: 150px auto">
    <div style="color: #ffffff;font-size: 40px;text-align: center;padding: 30px 0">Welcome</div>
      <el-form
               ref="form"
               size="normal"
               class="demo-ruleForm"
               :model="ruleForm"
               :rules="rules"
      >
      <el-form-item>
        <el-input v-model="ruleForm.workerId" placeholder="Please input"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="ruleForm.password" placeholder="Please input password" show-password></el-input>
      </el-form-item>


      <el-form-item>
      <el-button style="width:100%" type="primary" @click="login">Login</el-button>
      </el-form-item>
      </el-form>
      <el-button style="width: 100%"  @click="dialogFormVisible = true">Forget Password</el-button>

      <el-dialog v-model="dialogFormVisible" title="Reset Password">
        <el-form :model="ruleForm2">
          <el-form-item label="Verified Code"  :label-width="formLabelWidth">
            <el-input v-model="ruleForm2.code" placeholder="Please input verified code" autocomplete="off" />
            <el-button type="primary" @click="sendVerifiedCode">SEND</el-button>
          </el-form-item>
          <el-form-item label="New Password"  :label-width="formLabelWidth">
            <el-input v-model="ruleForm2.password1" placeholder="Please input password" autocomplete="off" show-password/>
          </el-form-item>
          <el-form-item label="Input Again" :label-width="formLabelWidth">
            <el-input v-model="ruleForm2.password2" placeholder="Please input password again" autocomplete="off" show-password/>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click=confirm>Confirm</el-button>
        </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { ElMessage } from 'element-plus'
import { h } from 'vue'
import Router from "@/router";
import Email from "../assets/js/smtp.js";
import md5 from 'js-md5';

export default {

  name: 'Login',
  data() {
    return {
      code: 0,
      codeLength: 4,
      random: [],
      msg: '',
      ruleForm: {
        workerId: '',
        password: '',
        email: '',
        phoneNumber: '',
        warehouseId: '',
        address: '',
        sex: '',
        age: '',
        workerName: '',
      },
      ruleForm2: {
        code: '',
        password1: "",
        password2: "",
      },
      rules: {
        workerId: [
          {
            required: true,
            message: "名称必填",
            trigger: "blur",
          }
        ],
        password: [
          {
            required: true,
            message: "名称必填",
            trigger: "blur",
          },
        ],
      },
      dialogFormVisible: false,
      formLabelWidth: '140px',

    }
  },
  methods: {
    login() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          console.log("Login successful")
          request.get ("http://localhost:9090/workers/" + this.ruleForm.workerId)
              .then(res => {//访问后端查询指定员工信息的Api，得到该员工信息

                this.ruleForm.warehouseId=res['warehouseId']
                console.log(md5(this.ruleForm.password))
                console.log(res["password"])
                console.log(res["warehouseId"])
                if (res["password"] === md5(this.ruleForm.password)) {
                  //因为进入查询页面后需要得到员工ID号和员工所属仓库ID进行信息筛选，先将workerId和warehouseId传至home页面
                  this.$router.push({
                    path: "/home",
                    query: {workerId: this.ruleForm.workerId,warehouseId: this.ruleForm.warehouseId}
                  })
                } else if (this.ruleForm.workerId === '' && this.ruleForm.password === '') {
                  ElMessage({
                    message: h('p', null, [
                      h('span', null, 'Please enter an ID'),
                    ]),
                  })//账号密码为空，提示输入账号
                } else if (res["password"] === undefined && this.ruleForm.workerId !== '' && this.ruleForm.password !== '') {
                  ElMessage({
                    message: h('p', null, [
                      h('span', null, 'Invalid ID!'),
                    ]),
                  })//输入数据库中不存在的账号，提示invalid ID
                } else if (this.ruleForm.password === '') {
                  ElMessage({
                    message: h('p', null, [
                      h('span', null, 'Please enter password'),
                    ]),
                  })//密码框为空时，提示输入密码
                } else if (res["password"] !== this.ruleForm.password) {
                  ElMessage({
                    message: h('p', null, [
                      h('span', null, 'Wrong password!'),
                    ]),
                  })//密码输入错误，提示密码错误
                }
              })
        }
      })
    },
    sendVerifiedCode() {

      this.code = Math.floor(Math.random()*10000)//随机生成四位数作为验证码
      this.code=this.code.toString()
      request.get("http://localhost:9090/workers/" + this.ruleForm.workerId)
          .then(res => {//访问后端指定员工信息，读取所有信息是为了方便之后更新

            this.ruleForm.email = res['email']//发送邮件需要的邮箱信息
            console.log(res['email'])
            console.log(this.code)
            this.ruleForm.workerId = res['workId']
            this.ruleForm.workerName = res['name']
            this.ruleForm.warehouseId = res['warehouseId']
            this.ruleForm.phoneNumber = res['telephone']
            this.ruleForm.address = res['address']
            this.ruleForm.age = res['age']
            this.ruleForm.sex = res['gender']
            Email.send({
              Host: "smtp.elasticemail.com",
              Username: "carosliu1989@gmail.com",
              Password: "6A943AD5E142B4F3D486FC8C1B29133C21FF",
              To: this.ruleForm.email,
              From: "carosliu1989@gmail.com",
              Subject: "Verified Code For Resetting Password",
              Body: this.code
            }).then(
                message => {
                  if (message === 'OK') {// 成功发送了邮件
                  } else {
                    console.error(message === 'NO')}
                });
          })
    },
    success() {
      ElMessage({
        showClose: true,
        message: 'success!',
        type: 'success',
      })
    },
    errorCode() {
      ElMessage({
        showClose: true,
        message: 'error code!',
        type: 'error',
      })
    },
    errorPassword() {
      ElMessage({
        showClose: true,
        message: 'two password are different!',
        type: 'error',
      })
    },
    confirm() {
      if (this.ruleForm2.code === this.code) {
        if(this.ruleForm2.password1===this.ruleForm2.password2){
          this.success();
          request.get("http://localhost:9090/workers/update?"
              +"workId="+this.ruleForm.workerId
              +"&password="+(md5(this.ruleForm2.password2))//访问后端更新员工信息的Api，将加密的密码值存入数据库
              +"&name="+this.ruleForm.workerName
              +"&email="+this.ruleForm.email
              +"&telephone="+this.ruleForm.phoneNumber
              +"&warehouseId="+this.ruleForm.warehouseId
              +"&gender="+this.ruleForm.sex
              +"&age="+this.ruleForm.age
              +"&address="+this.ruleForm.address
          )
          this.dialogFormVisible=false;
        }
        else {
          this.errorPassword();//提示两次输入密码不一致
        }
      } else {
        this.errorCode();//提示输入验证码错误
      }
    },
  }
}

</script>

<style scoped>

</style>