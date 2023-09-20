<template>
  <div>
    <router-view v-if="isShow"/>
  </div>

</template>

<script>
export default {
  name: "App",
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      isShow: true
    }
  },
  methods: {
    reload() {
      this.isShow = false;
      this.$nextTick(() => {
        this.isShow = true
      })
    }
  }
}

</script>