<template>

  <div style="padding: 10px">
    <span class="demonstration"></span>
    <el-date-picker
        v-model="date"
        type="daterange"
        range-separator="To"
        start-placeholder="Start"
        end-placeholder="End"
        style="margin-left: 300px"
    >
    </el-date-picker>

    <el-button type="primary" style="margin-left: 10px" @click="showAllGoods">All</el-button>
    <el-button type="primary" style="margin-left: 10px" @click="showTypes">Type</el-button>
    <el-button type="primary" style="margin-left: 10px" @click="showPositions">Position</el-button>
    <el-button type="primary" style="margin-left: 10px" @click="reset">Reset</el-button><br>
    <br>

    <div id="myChart" :style="{height:'400px',width:'80%'}"/>
  </div>
</template>

<script>
// 折线堆叠图
import resize from "@/mixins/resize";
 import * as echarts from "echarts";
import request from "@/utils/request";

export default {
  name: 'Graph' ,
  data(){
    return {
      date:[],
      goodData: {}, //货物数据
      dataOfAllGoods: [],
      dataOfAllTypes: [],
      dateAndTime: [], // 横坐标数组
      volume: [], // 纵坐标数组
      dateOnAxis:
        ['Please select a start date and an end date']

    }
  },

  created(){
    this.load();
  },
  mounted(){
    // this.getAllGood()
    // this.getAllTypes()
    // this.getAllPositions()
  },
  inject:[
    'reload'
  ],
  methods: {
    load(){

    },
    getAllGood () {
      let myChart = echarts.init(document.getElementById('myChart'))// 图标初始化
      request.get("http://localhost:9090/exchanges/")
      .then(res => {
        this.goodData = res

        let volume = []

        this.dateOnAxis=[]//real x-axis
        let currentDate='';
        currentDate=this.timestampToTime(this.date[0])
        for(let cnt=0;currentDate<=this.timestampToTime(this.date[1]);cnt++){

          this.dateOnAxis.push(this.timestampToTime(currentDate));
          currentDate=this.addDate(currentDate)
        }
        console.log(this.dateOnAxis)
        let currentVolume=0;

        for(let cnt =0;cnt < this.dateOnAxis.length;cnt++){
          currentVolume=0;
          for(let i=0; i<this.goodData.length; i++) {
            if(this.goodData[i].warehouseId===this.$route.query.warehouseId) {//记得改成传递进来的仓库ID
              // volumeOfFood.push(this.goodData[i].volume)
              // dateAndTime.push(this.goodData[i].dateAndTime)
              //把this.date[0]到this.date[1]转化成日期格式每天记录到this.dateAndTime[]数组里
              //将后端数据中的时间戳转成日期格式
              //遍历this.dateAndTime[]数组{遍历后端数据数组，如果有相同日期的货物，相加，放进volume[i]数组，表示纵坐标}
              //各类型的类似，循环套循环，if条件是相同日期并且相同类型，volume相加，放进volumeOfFood[i]数组，表示纵坐标
              console.log(this.dateOnAxis.length)
              if (this.timestampToTime(this.goodData[i].dateAndTime) === this.dateOnAxis[cnt]) {
                currentVolume = this.goodData[i].volume + currentVolume
                console.log(1)
                console.log(this.goodData[i].volume)
              }
            }
          }
          volume.push(currentVolume)

        }

          myChart.setOption({
            title: {
              text: 'Statistic'
            },
            tooltip: {
              trigger: 'axis' ,  // axis   item   none三个值
              showContent:false
            },
            legend: {
              data:['all']
            },
            xAxis:{
              name: 'Date',
              data: this.dateOnAxis
            },
            yAxis:{
              name:'Volume',
              type:'value',
            },
            series:[
              {
                name: 'all',
                type: 'line', // 类型为z折线图
                data: volume,
                stack: 'x',
                itemStyle: {
                  color: '#ef6567',
                  width: 4
                }
              },
            ]
          })
      })
// 随着屏幕大小调节图表
      window.addEventListener('resize', () => {
        myChart.resize()
      })


    },
    getAllTypes(){
      let myChart = echarts.init(document.getElementById('myChart'))
      request.get("http://localhost:9090/exchanges/")
      .then(res => {
        this.goodData =res
        let volumeOfFood=[];
        let volumeOfSTATIONARY=[];
        let volumeOfFruit=[];
        let volumeOf3C=[];

        this.dateOnAxis=[];
        let currentDate='';
        currentDate=this.timestampToTime(this.date[0])
        for(let cnt=0;currentDate<=this.timestampToTime(this.date[1]);cnt++){
          this.dateOnAxis.push(this.timestampToTime(currentDate));
          currentDate=this.addDate(currentDate)
        }

        let currentVolumeOfFood=0;
        let currentVolumeOfSTATIONARY=0;
        let currentVolumeOfFruit=0;
        let currentVolumeOf3C=0;

        for(let cnt =0;cnt < this.dateOnAxis.length;cnt++){
          currentVolumeOfFood=0;
          currentVolumeOfSTATIONARY=0;
          currentVolumeOfFruit=0;
          currentVolumeOf3C=0;
          for(let i=0; i<this.goodData.length; i++) {
            if(this.goodData[i].warehouseId===this.$route.query.warehouseId) {
              if (this.timestampToTime(this.goodData[i].dateAndTime) === this.dateOnAxis[cnt]&&this.goodData[i].type==='Food') {
                currentVolumeOfFood = this.goodData[i].volume + currentVolumeOfFood
              }
              if(this.timestampToTime(this.goodData[i].dateAndTime)===this.dateOnAxis[cnt]&&this.goodData[i].type==='STATIONARY'){
                currentVolumeOfSTATIONARY = this.goodData[i].volume + currentVolumeOfSTATIONARY
              }
              if(this.timestampToTime(this.goodData[i].dateAndTime)===this.dateOnAxis[cnt]&&this.goodData[i].type==='Fruit'){
                currentVolumeOfFruit = this.goodData[i].volume + currentVolumeOfFruit
              }
              if(this.timestampToTime(this.goodData[i].dateAndTime)===this.dateOnAxis[cnt]&&this.goodData[i].type==='3C'){
                currentVolumeOf3C = this.goodData[i].volume + currentVolumeOf3C
              }
            }
          }
          volumeOfFood.push(currentVolumeOfFood)
          volumeOfSTATIONARY.push(currentVolumeOfSTATIONARY)
          volumeOfFruit.push(currentVolumeOfFruit)
          volumeOf3C.push(currentVolumeOf3C)
        }

        myChart.setOption({
          title: {
            text: 'Statistic'
          },
          tooltip: {
            trigger: 'axis'   // axis   item   none三个值
          },
          legend: {
            data: ['Food','STATIONARY','3C','Fruit']

          },
          xAxis:{
            name: 'Date',
            data: this.dateOnAxis
          },
          yAxis:{
            name:'Volume',
            type:'value',
            data: [0,50,100,150,200,250]
          },
          series:[
            {
              name: 'Food',
              type: 'line', // 类型为z折线图
              data: volumeOfFood,
              // stack: 'x',
              itemStyle: {
                color: '#3234f6',
                width: 4
              }
            },
            {
              name: 'STATIONARY',
              type: 'line',
              data: volumeOfSTATIONARY,
              // stack: 'x',
              itemStyle:{
                color: '#4fa81c',
                width: 4
              }
            },
            {
              name: '3C',
              type: 'line',
              data: volumeOf3C,
              // stack: 'x',
              itemStyle:{
                color: 'rgba(239,85,143,0.85)',
                width: 4
              }
            },
            {
              name: 'Fruit',
              type: 'line',
              data: volumeOfFruit,
              // stack: 'x',
              itemStyle:{
                color: 'rgba(232,192,31,0.97)',
                width: 4
              }
            },
          ]
        })


      })
      window.addEventListener('resize', () => {
        myChart.resize()
      })
    },
    getAllPositions(){
      let myChart = echarts.init(document.getElementById('myChart'))
      request.get("http://localhost:9090/exchanges/")
          .then(res => {
            this.goodData =res
            let volumeOf010101=[];
            let volumeOf010103=[];
            let volumeOf010201=[];
            let volumeOf010202=[];

            this.dateOnAxis=[];
            let currentDate='';
            currentDate=this.timestampToTime(this.date[0])
            for(let cnt=0;currentDate<=this.timestampToTime(this.date[1]);cnt++){
              this.dateOnAxis.push(this.timestampToTime(currentDate));
              currentDate=this.addDate(currentDate)
            }

            let currentVolumeOf010101=0;
            let currentVolumeOf010201=0;
            let currentVolumeOf010103=0;
            let currentVolumeOf010202=0;

            for(let cnt =0;cnt < this.dateOnAxis.length;cnt++){
              currentVolumeOf010101=0;
              currentVolumeOf010201=0;
              currentVolumeOf010103=0;
              currentVolumeOf010202=0;
              for(let i=0; i<this.goodData.length; i++) {
                if(this.goodData[i].warehouseId===this.$route.query.warehouseId) {
                  if (this.timestampToTime(this.goodData[i].dateAndTime) === this.dateOnAxis[cnt]&&this.goodData[i].position==='W01-S01-P01') {
                    currentVolumeOf010101 = this.goodData[i].volume + currentVolumeOf010101
                  }
                  if(this.timestampToTime(this.goodData[i].dateAndTime)===this.dateOnAxis[cnt]&&this.goodData[i].position==='W01-S02-P01'){
                    currentVolumeOf010201 = this.goodData[i].volume + currentVolumeOf010201
                  }
                  if(this.timestampToTime(this.goodData[i].dateAndTime)===this.dateOnAxis[cnt]&&this.goodData[i].position==='W01-S01-P03'){
                    currentVolumeOf010103 = this.goodData[i].volume + currentVolumeOf010103
                  }
                  if(this.timestampToTime(this.goodData[i].dateAndTime)===this.dateOnAxis[cnt]&&this.goodData[i].position==='W01-S02-P02'){
                    currentVolumeOf010202 = this.goodData[i].volume + currentVolumeOf010202
                  }
                }
              }
              volumeOf010101.push(currentVolumeOf010101)
              volumeOf010201.push(currentVolumeOf010201)
              volumeOf010103.push(currentVolumeOf010103)
              volumeOf010202.push(currentVolumeOf010202)
            }

            myChart.setOption({
              title: {
                text: 'Statistic'
              },
              tooltip: {
                trigger: 'axis'   // axis   item   none三个值
              },
              legend: {
                data: ['W01-S01-P01','W01-S02-P01','W01-S01-P03','W01-S02-P02']

              },
              xAxis:{
                name: 'Date',
                data: this.dateOnAxis
              },
              yAxis:{
                name:'Volume',
                type:'value',
                data: [0,50,100,150,200,250]
              },
              series:[
                {
                  name: 'W01-S01-P01',
                  type: 'line', // 类型为z折线图
                  data: volumeOf010101,
                  // stack: 'x',
                  itemStyle: {
                    color: '#3234f6',
                    width: 4
                  }
                },
                {
                  name: 'W01-S02-P01',
                  type: 'line',
                  data: volumeOf010201,
                  // stack: 'x',
                  itemStyle:{
                    color: '#4fa81c',
                    width: 4
                  }
                },
                {
                  name: 'W01-S01-P03',
                  type: 'line',
                  data: volumeOf010103,
                  // stack: 'x',
                  itemStyle:{
                    color: 'rgba(239,85,143,0.85)',
                    width: 4
                  }
                },
                {
                  name: 'W01-S02-P02',
                  type: 'line',
                  data: volumeOf010202,
                  // stack: 'x',
                  itemStyle:{
                    color: 'rgba(232,192,31,0.97)',
                    width: 4
                  }
                },
              ]
            })

          })
      window.addEventListener('resize', () => {
        myChart.resize()
      })
    },

    showAllGoods(){
      this.getAllGood()
    },
    showTypes(){
      this.getAllTypes()
    },
    showPositions(){
      this.getAllPositions()
    },
    timestampToTime(timestamp) {
      if(timestamp===0 || timestamp==null){
        return ''
      }else{
        let date = new Date(timestamp)
        let Y = date.getFullYear()+'-'
        let M = (date.getMonth() + 1 < 10 ? '0' +(date.getMonth()+1) : date.getMonth() + 1) +'-'
        let D = date.getDate() <10 ? '0' + date.getDate() : date.getDate()
        return Y + M + D
      }
    },
    addDate(time){
      console.log(time)
      let newDate =new Date(time)
      newDate=this.timestampToTime(newDate.setDate(newDate.getDate()+1));
      console.log(newDate)
      return newDate
    },
    reset(){
      this.reload();
    },
  },


}
</script>



<style scoped>

</style>