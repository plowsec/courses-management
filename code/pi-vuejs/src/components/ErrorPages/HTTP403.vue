<template>
  <div class="container" align="center">
    <div class="cover">
      <h1>Access Denied
        <small>Error 403</small>
      </h1>
      <p class="lead">The requested resource requires an authentication.</p>
    </div>
    <button v-on:click="back" type="button" class="btn btn-default">Go back</button>
  </div>
</template>

<script>
  export default {
    name: "HTTP403",
    methods: {
      back: function (event) {
        window.history.go(-2);
      }
    }
  }
</script>

<style scoped>

</style>
