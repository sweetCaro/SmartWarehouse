<template>
  <div style="height: 50px; line-height:  50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 20px;font-weight: bold;color: cornflowerblue">{{this.$route.query.workerId}}</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;font-weight: bold;color: cornflowerblue">
    <el-dropdown>
    <span class="el-dropdown-link">
      <br>setting
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="person">Personal</el-dropdown-item>
        <el-dropdown-item @click="logout">Exit</el-dropdown-item>
      </el-dropdown-menu>
    </template>
    </el-dropdown>
    </div>
    <div style="margin: 10px 0">

      <el-dialog v-model="dialogVisible" title="Personal Information" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="Warehouse Id:">{{form.warehouseId}}</el-form-item>
          <el-form-item label="Worker Id:">{{this.$route.query.workerId}}</el-form-item>
          <el-form-item label="Name:">{{form.workerName}}</el-form-item>
          <el-form-item label="age:">{{form.age}}</el-form-item>
          <el-form-item label="sex:">{{form.sex}}</el-form-item>
          <el-form-item label="phone:">{{form.phoneNumber}}</el-form-item>
          <el-form-item label="email:">{{form.email}}</el-form-item>
          <el-form-item label="address:">{{form.address}}</el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="resetPassword">reset password</el-button>
            <el-button type="primary" @click="modify">modify</el-button>
          </span>
        </template>
      </el-dialog>
    </div>

    <div style="margin: 10px 0">
      <el-dialog v-model="dialogVisible2" title="Personal Information" width="30%">
        <el-form
            ref="ruleFormFef"
            :model="ruleForm"
            :rules="rules"
            label-width="120px"
            class="demo-ruleForm"
            :size="form.size"
            status-icon
        >
          <el-form-item label="phone:" prop="phoneNumber">
            <el-input v-model="ruleForm.phoneNumber" />
          </el-form-item>
          <el-form-item label="email:" prop="email">
            <el-input v-model="ruleForm.email" />
          </el-form-item>
          <el-form-item label="address:" prop="address">
            <el-input v-model="ruleForm.address" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="cancelModify()" size="small" class="dialog-btn"
            >cancel</el-button>
            <el-button @click="submitModify" type="primary" class="dialog-btn"
            >save</el-button>
          </span>
        </template>
      </el-dialog>
    </div>

    <div style="margin: 10px 0">
      <el-dialog v-model="dialogVisible3" title="Reset Password" width="30%">
        <el-form :model="ruleForm2" label-width="120px">
        </el-form>
        <el-form-item label="New Password:" prop="password" >
          <el-input v-model="ruleForm2.password1" show-password/>
        </el-form-item>
        <el-form-item label="Input Again:" prop="password">
          <el-input v-model="ruleForm2.password2" show-password/>
        </el-form-item>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible3 = false">cancel</el-button>
            <el-button type="primary" @click="submitResetPassword">save</el-button>
          </span>
        </template>
      </el-dialog>
    </div>


  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import md5 from 'js-md5';
export default {
  name: "Header",
  components: {

  },
  data(){
    // 自定义邮箱规则
    const checkEmail = (rule, value, callback) => {
      const regEmail = /^\w+@\w+(\.\w+)+$/
      if (regEmail.test(value)) {
        // 合法邮箱
        return callback()
      }
      callback(new Error('please enter a correct email address'))
    };


    return{
      form:[
        {
          warehouseId:'',
        },
        {
          workerId:'',
        },
        {
          workerName:'',
        },
        {
          age:'',
        },
        {
          sex:'',
        },
        {
          phoneNumber:'',
        },
        {
          email :'',
        },
        {
          address:'',
        },

      ],
      ruleForm:{
        phoneNumber:'',
        email :'',
        address :'',
      },
      rules:({
        phoneNumber:[
          {required:true,massage:'please enter a phone number',trigger:'blur'},
          {min:6,max:11,massage: 'length should be 6-11',trigger: 'blur'}

        ],
        email:[
          {
            required:true,
            massage:'please enter a email address',trigger:'blur'
          },
          {
            validator:checkEmail,trigger: 'blur'
          },
        ],
        address:[
          {
            required:true,
            massage:'please enter an address',
            trigger:'blur'
          },

        ]
      }),
      ruleForm2:{
        password1:[],
        password2:[],
      },
      dialogVisible: false,
      dialogVisible2:false,
      dialogVisible3:false,
      workerTable: {},
    }
  },


  methods: {
    person(){
      request.get("http://localhost:9090/workers/"+this.$route.query.workerId)
        .then(res => {//获取后端显示的指定员工信息并在个人信息弹窗中显示
            this.form.workerId = res.workId;
            this.form.password = res.password;
            this.form.warehouseId=res.warehouseId;
            this.form.workerName=res.name;
            this.form.age=res.age;
            this.form.sex=res.gender;
            this.form.email=res.email;
            this.form.phoneNumber=res.telephone;
            this.form.address=res.address;
        })
      this.dialogVisible= true;
      this.form={}
    },
    resetPassword(){
      this.dialogVisible3= true;
      this.dialogVisible=false;
    },
    submitResetPassword(){
      if(this.ruleForm2.password1===this.ruleForm2.password2) {
        request.get("http://localhost:9090/workers/update?"
            + "workId=" + this.form.workerId
            + "&password=" + md5(this.ruleForm2.password2)//将输入的新密码存为md5加密后的值
            + "&name=" + this.form.workerName
            + "&email=" + this.form.email
            + "&telephone=" + this.form.phoneNumber
            + "&warehouseId=" + this.form.warehouseId
            + "&gender=" + this.form.sex
            + "&age=" + this.form.age
            + "&address=" + this.ruleForm.address
        )
        this.success();//修改成功出现成功提示
      }
      else this.error();//修改失败出现失败提示
    },
    success() {
      ElMessage({
        showClose: true,
        message: 'success!',
        type: 'success',
      })
    },
    error() {
      ElMessage({
        showClose: true,
        message: 'error!',
        type: 'error',
      })
    },
    modify(){
      this.dialogVisible2=true;
      this.ruleForm.phoneNumber=this.form.phoneNumber
      this.ruleForm.email=this.form.email
      this.ruleForm.address=this.form.address//先把原来的值填进去
      this.dialogVisible=false;
    },
    submitModify(){
      request.get("http://localhost:9090/workers/update?"
          +"workId="+this.form.workerId
          +"&password="+this.form.password
          +"&name="+this.form.workerName
          +"&email="+this.ruleForm.email//输入的新邮箱
          +"&telephone="+this.ruleForm.phoneNumber//输入的新电话号码
          +"&warehouseId="+this.form.warehouseId
          +"&gender="+this.form.sex
          +"&age="+this.form.age
          +"&address="+this.ruleForm.address//输入的新地址
      )//访问后端更新员工信息的API
          .then(res => {
            console.log(res);
          })
      this.dialogVisible2= false;
    },
    cancelModify(){
      this.dialogVisible2= false;
    },

    logout(){//点击exit按钮后回到login页面，即退出
      this.$router.push("/login")
    }
  },
}
</script>

<style scoped>

</style>