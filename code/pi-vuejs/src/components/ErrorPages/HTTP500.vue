<template>
  <div class="container" align="center">
    <div class="cover">
      <h1>Webservice currently unavailable
        <small>Error 500</small>
      </h1>
      <p class="lead">An unexpected condition was encountered.<br/>Our service team has been dispatched to bring it back
        online.</p>
    </div>
    <button v-on:click="back" type="button" class="btn btn-default">Go back</button>
  </div>
</template>

<script>
  export default {
    name: "HTTP500",
    methods: {
      back: function (event) {
        window.history.go(-1);
      }
    }
  }
</script>

<style scoped>

</style>
