<template>
    <el-col :span="4">
      <el-menu
          default-active="2"
          class="el-menu-vertical-demo"
      >
        <el-menu-item index="2">
          <span @click="search">Query information</span>
        </el-menu-item>
        <el-menu-item index="4">
          <span @click="graph">Statistic & Graph</span>
        </el-menu-item>
      </el-menu>
    </el-col>

</template>

<script>
export default {
  name: "Aside",
  methods:{
    search(){
      this.$router.push({
        path :  "/home"+"?workerId="+this.$route.query.workerId+"&warehouseId="+this.$route.query.warehouseId,
        query:{workerId:this.$route.query.workerId,warehouseId:this.$route.query.warehouseId}
      })
    },
    graph(){
      this.$router.push({
        path :  "/graph"+"?workerId="+this.$route.query.workerId+"&warehouseId="+this.$route.query.warehouseId,
        query:{workerId:this.$route.query.workerId,warehouseId:this.$route.query.warehouseId}
      })
    }
  }
}
</script>

<style scoped>

</style>