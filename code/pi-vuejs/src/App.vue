<template>
  <div id="app" class="padding-bottom">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <div class="navbar-header" v-if="this.$global.user_role">
          <a class="navbar-brand" href="#/home">Courses Management</a>
        </div>
        <div class="navbar-header" v-else>
          <a class="navbar-brand">Courses Management</a>
        </div>
        <ul class="nav navbar-nav navbar-right" v-if="this.$global.user_role">
          <li><a class="padding-right">Signed in as {{ this.$global.user_firstname }} {{ this.$global.user_lastname }}</a></li>
          <li><a href="#" v-on:click="logout()">Logout</a></li>
        </ul>
      </div>
    </nav>
    <v-dialog />
    <router-view/>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'App',
    watch: {
      user_role: function (val) {
        this.$global.user_role = localStorage.getItem('role');
      },
      user_firstname: function (val) {
        this.$global.user_firstname = localStorage.getItem('firstname');
      },
      user_lastname: function (val) {
        this.$global.user_lastname = localStorage.getItem('lastname');
      },
      user_username: function (val) {
        this.$global.user_username = localStorage.getItem('username');
      }
    },
    created() {
      this.$global.user_role = localStorage.getItem('role');
      this.$global.user_firstname = localStorage.getItem('firstname');
      this.$global.user_lastname = localStorage.getItem('lastname');
      this.$global.user_username = localStorage.getItem('username');
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

<style lang="css">
  nav {
    margin-bottom: 2em;
    color: whitesmoke;
  }

  .padding-right {
    padding-right: 1em;
  }

  .padding-bottom {
    padding-bottom: 5em;
  }

  @import 'css/app.css';
</style>
