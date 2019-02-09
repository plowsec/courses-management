<template>
  <div class="container" align="center">
    <div class="cover">
      <h1>Registration period closed</h1>
      <p class="lead">The registration period is closed !</p>
    </div>
    <button v-on:click="logout()" type="button" class="btn btn-default">Logout</button>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: "RegistrationsClosed",
    data() {
      return {
        user_role: localStorage.getItem('role'),
        currentDateDebut: "",
        currentDateFin: "",
        tempCurrentDateDebut: "",
        tempCurrentDateFin: "",
        newDateDebut: "",
        newDateFin: "",
        isPeriodOK: false
      }
    },
    created() {
    },
    methods: {
      logout() {
        this.$modal.show("dialog", {
          title: 'Warning',
          text: 'Are you sure you want to log out ?',
          buttons: [
            {
              title: 'Logout',
              handler: () => {
                axios.get('http://localhost:8081/logout', {withCredentials: true})
                  .then(response => {
                    localStorage.removeItem('role');
                    this.role = null;
                    this.$global.user_role = null;
                    localStorage.removeItem('firstname');
                    this.firstname = null;
                    this.$global.user_firstname = null;
                    localStorage.removeItem('lastname');
                    this.lastname = null;
                    this.$global.user_lastname = null;
                    localStorage.removeItem('username');
                    this.username = null;
                    this.$global.user_username = null;
                    this.$router.push({path: '/'});
                    this.$modal.hide('dialog');
                  })
                  .catch(e => {
                    alert(e.message);
                  })
              }
            },
            {
              title: 'Cancel',
              default: true
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
