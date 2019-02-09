<template>
  <div class="container" align="center">
    <h2 class="display-4">Students List</h2>
    <br/>
    <table class="table table-bordered table-hover">
      <thead>
      <tr>
        <th>Lastname</th>
        <th>Firstname</th>
        <th>Username</th>
      </tr>
      </thead>
      <tbody>
      <tr v-on:click="seeRegs(userName)" v-for="{lastName, firstName, userName} in listEleve" :key="userName">
        <td>
          {{lastName}}
        </td>
        <td>
          {{firstName}}
        </td>
        <td>
          {{userName}}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'ListEleves',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        listEleve: []
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (['HEADTEACHER', 'TEACHER'].indexOf(this.user_role) < 0) {
        this.$router.push({path: '/403'});
      } else {
        axios.get('http://localhost:8081/users/students', {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.listEleve = response.data
              this.pushLinks()
            } else if (response.status == '400') {
              this.$router.push({path: '/400'});
            } else if (response.status == '401') {
              this.$router.push({path: '/401'});
            } else if (response.status == '403') {
              this.$router.push({path: '/403'});
            } else if (response.status == '500') {
              this.$router.push({path: '/500'});
            }
          })
          .catch(e => {
            this.listEleve = e.message
            this.errors.push(e)
          })
      }
    },
    methods: {
      pushLinks() {
        for (var eleve in this.listEleve) {
          var userId = this.listEleve[eleve].userName;
          //console.log(this.listEleve[eleve]);
          //console.log(userId);
          //this.$router.push({ path: '/Registrations/${userId}' })
        }
      },
      seeRegs(userName) {
        //this.$router.push({ name: 'Registrations',params: { userName } })
        this.$router.push({path: `/student/${userName}`})
      }
    }
  }
</script>

<style lang="css">
  h1 {
    margin-bottom: 1em;
  }

  table tbody tr {
    cursor: pointer;
  }

  @import '../css/addUser.css';
</style>
