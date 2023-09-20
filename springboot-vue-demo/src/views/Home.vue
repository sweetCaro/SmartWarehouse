<template>
  <div style="padding: 10px">
    <div>
      <el-select v-model="value1" placeholder="Select Type" >
        <el-option
            v-for="item in types"
            :key="item.value1"
            :label="item.label1"
            :value="item.value1"
        >
          <span style=" float: left " >{{ item.label1 }}</span>
          <span style=" float: right;
                        color: var(--el-text-color-secondary);
                        font-size: 13px; ">{{ item.value1 }}</span>
        </el-option>
      </el-select>
      <el-select style="padding-left: 10px" v-model="value2" placeholder="Select Position">
        <el-option
            v-for="item in position"
            :key="item.value2"
            :label="item.label2"
            :value="item.value2"
        >
          <span style=" float: left">{{ item.label2 }}</span>
          <span style=" float: right;
                        color: var(--el-text-color-secondary);
                        font-size: 13px; ">{{ item.value2 }}</span>
        </el-option>
      </el-select><br>
      <br>
      <span class="demonstration"></span>
      <el-date-picker
          v-model="value"
          type="datetimerange"
          range-separator="To"
          start-placeholder="Start"
          end-placeholder="End"
      >
      </el-date-picker>

      <el-button type="primary" style="margin-left: 5px" @click="search">SEARCH</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="update">RESET</el-button><br>

    </div>
    <br>
    <div>
      <el-pagination
          @size-change="sizeChange" @current-change="currentChange"
          :current-page="page" :page-size="size" :page-sizes="pageSizes"
          layout="total,sizes,prev,pager,next,jumper" :total="this.allData.length">
      </el-pagination>
        <el-table :data="tableData"  border stripe style="width: 100%">
          <el-table-column prop="goodId" label="goodId" sortable/>
          <el-table-column prop="type" label="type" />
          <el-table-column prop="position" label="position" />
          <el-table-column prop="dateAndTime" label="dateAndTime" sortable/>
          <el-table-column prop="transformation" label="transformation"/>
          <el-table-column prop="volume" label="volume"/>
      </el-table>
    </div>
  </div>

</template>

<script >

import request from "@/utils/request";
import {Search, Edit, Check, Message, Star, Delete, ArrowDown,} from "@element-plus/icons";


export default {
  name: 'Home',
  components:{
    Search,
    Edit,
    Check,
    Message,
    Star,
    Delete,
    ArrowDown,
  },
  data(){
    return {
      volume:0,
      warehouseId:'',
      form: {},
      selectTableData1:[],
      selectTableData2:[],
      selectTableData3:[],
      tableData: [],
      resultData: [],
      tableDataTemp:[],
      allData:[],//所有数据
      total:0,
      size:10,//一页有多少条数据
      page:1,//当前是第几页
      pageSizes:[5,10,15],//有几个页面尺寸供选择
      dateAndTime:[],

      value : [],
      value1 :'',
      types:  [
        {
          value1: 'all',
          label1: 'all',
        },
        {
          value1: 'Food',
          label1: 'Food',
        },
        {
          value1: 'STATIONARY',
          label1: 'STATIONARY',
        },
        {
          value1: 'Fruit',
          label1: 'Fruit',
        },
        {
          value1: '3C',
          label1: '3C',
        },

      ],
      value2 :'',
      position:  [
        {
          value2: 'all',
          label2: 'all',
        },
        {
          value2: 'W01-S01-P01',
          label2: 'W01-S01-P01',
        },
        {
          value2: 'W01-S01-P02',
          label2: 'W01-S01-P02',
        },
        {
          value2: 'W01-S02-P01',
          label2: 'W01-S02-P01',
        },
        {
          value2: 'W02-S01-P01',
          label2: 'W02-S01-P01',
        },
        {
          value2: 'W02-S01-P02',
          label2: 'W02-S01-P02',
        },
        {
          value2: 'W03-S01-P01',
          label2: 'W03-S01-P01',
        },
      ],
    }
  },


  inject:[
    'reload'
  ],
  created() {
    this.load()
  },
  methods:{
    load(){
      request.get("http://localhost:9090/exchanges", {
      //访问后端获取所有进出仓货物信息的Api
      }).then(res => {
        this.warehouseId=this.$route.query.warehouseId;//读取员工所属仓库ID号
        this.tableDataTemp=res//先将所有信息存入暂存数组

        console.log(this.warehouseId)
        for(let i = 0; i < this.tableDataTemp.length; i++) {
          if (this.tableDataTemp[i].warehouseId===this.warehouseId){
            this.resultData.push(this.tableDataTemp[i]);
          }
        }//只将该所属仓库的进出仓货物信息存入需要的信息表中
        console.log(this.resultData[0].warehouseId)

        this.tableData = this.resultData;
        this.allData=this.resultData;
        //设置翻页
        this.tableData=this.allData.slice((this.page - 1 )*this.size,this.page*this.size);
      })
    },
    findForType(){//按类型筛选
      this.selectTableData1=[];
      if(this.value1!=="all"){//当选择值不为all,显示对应的类型货物信息
        for(let i = 0; i < this.allData.length ; i++){
          if(this.allData[i].type.indexOf(this.value1)>=0){
            this.selectTableData1.push(this.allData[i]);
          }
        }
      }
      if(this.value1==="all"){//当选择值为all,显示所有类型货物信息
        for(let i = 0; i < this.allData.length ; i++){
            this.selectTableData1.push(this.allData[i]);
        }
      }
      this.allData=this.selectTableData1;//更新信息表
    },
    findForPosition(){//按位置筛选
      this.selectTableData2=[];
       if(this.value2!=="all"){
         for(let i = 0; i < this.allData.length ; i++){
           if(this.allData[i].position.indexOf(this.value2)>=0){
             this.selectTableData2.push(this.allData[i]);
           }
         }
       }//当选择值不为all,显示对应位置的货物信息
       if(this.value2==="all"){
        for(let i = 0; i < this.allData.length ; i++){
          this.selectTableData2.push(this.allData[i]);
        }
      }//当选择值为all,显示所有货物的位置信息
      this.allData=this.selectTableData2;//更新信息表
    },
    findForTimeRange(){
      this.selectTableData3=[];
      for(let i = 0; i < this.allData.length; i++){//日期时间对比中需要将日期格式均转为object才能进行比较
        //使用new Date(Date.parse())将信息表中的日期时间转为object格式
        if(new Date(Date.parse(this.allData[i].dateAndTime)) >this.value[0]
            && new Date(Date.parse(this.allData[i].dateAndTime)) < this.value[1]){
          this.selectTableData3.push(this.allData[i]);
        }}
      this.allData=this.selectTableData3;//更新信息表
    },
    search(){
      this.findForPosition(); this.findForType(); this.findForTimeRange();
      this.page=1; //点击SEARCH得到筛选结果后需要重新回到第一页
      this.getTableData();//设置分页
    },
    getTableData(){
      this.tableData=this.allData.slice((this.page - 1 )*this.size,
          this.page*this.size);
      this.total=this.allData.length
    },
    currentChange (val) {//当前是第几页
      this.page=val;
      this.getTableData()
    },
    sizeChange(val){//选择单个页面数据量
      this.size=val;
      this.page=1;
      this.getTableData();
    },
    update(){
      this.reload()
      console.log('刷新页面')
    }
   },


}
</script>
