<template>
  <div id="login" class="container" align="center">
    <h2 class="display-4">Login</h2>
    <br/>
    <div class="row">
      <div class="col-md-offset-3 col-md-3"></div>
      <div class="col-md-offset-6 col-md-6">
        <div class="form-login">
          <input type="text" name="username" v-model="userName" class="form-control input-sm chat-input"
                 placeholder="Username"/>
          <br/>
          <input type="password" name="password" v-model="userPassword" class="form-control input-sm chat-input"
                 placeholder="Password"/>
          <br/>
          <div class="wrapper">
            <button v-on:click="postAdd()" class="btn btn-primary" type="submit">Login</button>
          </div>
        </div>
      </div>
      <div class="col-md-offset-3 col-md-3"></div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
 const https = require('https');
  export default {
    name: "Login",
    data() {
      return {
        user_role: localStorage.getItem('role'),
        userName: "",
        userPassword: ""
      }
    },
    methods: {
      postAdd(event) {
        if (!this.user_role) {
		  var data = "username=" + this.userName + "&password=" + this.userPassword;
		  const agent = new https.Agent({
  				rejectUnauthorized: false
			});
          axios.post('http://localhost:8081/login', data, {withCredentials: true, httpsAgent: agent
	})
            .then(response => {
              if (response.status == '200') {
                axios.get('http://localhost:8081/whoami', {withCredentials: true})
                  .then(response => {
                    localStorage.setItem('role', response.data.role);
                    this.$global.user_role = localStorage.getItem('role');
                    localStorage.setItem('firstname', response.data.firstName);
                    this.$global.user_firstname = localStorage.getItem('firstname');
                    localStorage.setItem('lastname', response.data.lastName);
                    this.$global.user_lastname = localStorage.getItem('lastname');
                    localStorage.setItem('username', response.data.userName);
                    this.$global.user_username = localStorage.getItem('username');
                    if (this.$global.user_role == 'ADMIN')
                      this.$router.push({path: '/user/create'});
                    else
                      this.$router.push({path: '/home'});
                  })
                  .catch(e => {
                    alert(e.message);
                  })
              }
            })
            .catch(e => {
              alert("Wrong username/password !");
            })
        } else {
          alert("You're already logged in !");
        }
      }
    }
  }
</script>
