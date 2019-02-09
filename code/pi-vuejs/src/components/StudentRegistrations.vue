<template>
  <div class="container" align="center">
    <h2 class="display-4">Registrations of {{student}}</h2>
    <br/>
    <table class="table table-bordered">
      <thead>
      <tr>
        <th>ID</th>
        <th>Course</th>
        <th>Priority</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="{cours, priorite } in registrationList[student]" :key="cours.coursId">
        <td>
          {{cours.coursId}}
        </td>
        <td>
          {{cours.titre}}
        </td>
        <td>
          {{priorite}}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'StudentRegistrations',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        user_username: localStorage.getItem('username'),
        registrationList: [],
        student: this.$route.params.id
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (['HEADTEACHER', 'TEACHER'].indexOf(this.user_role) > -1 || this.$route.params.id == this.user_username) {
        axios.get(`http://localhost:8081/users/${this.$route.params.id}/registrations/`, {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.registrationList = response.data
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
            this.registrationList = e.message
            this.errors.push(e)
          })
      } else {
        this.$router.push({path: '/403'});
      }
    }
  }
</script>

<style scoped>
  h1 {
    margin-bottom: 1em;
  }

  table tbody tr {
    cursor: default;
  }
</style>
