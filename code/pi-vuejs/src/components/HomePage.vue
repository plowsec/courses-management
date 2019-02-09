<template>
  <div class="container" align="center">
    <h2 class="display-4">Home</h2>
    <br/>
    <div class="alert alert-danger" v-if="showError">
      {{error}}
    </div>
    <p>Registration period starts {{dateDebut | formatDate}} and ends {{dateFin | formatDate}}.</p>
    <div class="container">
      <div class="card-deck">
        <div class="card" v-if="this.$global.user_role != 'ADMIN' ">
          <img style="" class="card-img-top" src="../assets/list2.png" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Courses list</h5>
            <p class="card-text">Consult the proposed courses.</p>
          </div>
          <div class="card-footer">
            <a class="btn btn-primary" href="#/courses">Go</a>
          </div>
        </div>
        <div class="card" v-else>
        </div>
        <div class="card" v-if="this.$global.user_role == 'HEADTEACHER' || this.$global.user_role == 'TEACHER' ">
          <img style="" class="card-img-top" src="../assets/student.png" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Students list</h5>
            <p class="card-text">Manage your students and consult their current registrations.</p>
          </div>
          <div class="card-footer">
            <a class="btn btn-primary" href="#/students">Go</a>
          </div>
        </div>
        <div class="card" v-else-if="this.$global.user_role == 'STUDENT' ">
          <img style="" class="card-img-top" src="../assets/registrations.png" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">My registrations</h5>
            <p class="card-text">Consult your current registrations</p>
          </div>
          <div class="card-footer">
            <a class="btn btn-primary" :href="'#/student/'+this.user_username">Go</a>
          </div>
        </div>
        <div class="card" v-if="this.$global.user_role == 'HEADTEACHER' || this.$global.user_role == 'TEACHER' ">
          <img class="card-img-top" src="../assets/eye.png" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Summary</h5>
            <p class="card-text">Summary of all the courses and students, with the current state of their
              registrations.</p>
          </div>
          <div class="card-footer">
            <a class="btn btn-primary" href="#/summary">Go</a>
          </div>
        </div>
        <div class="card" v-else>
        </div>
      </div>
      <div class="card-deck">
        <div class="card" v-if="this.$global.user_role == 'HEADTEACHER'">
          <img class="card-img-top " src="../assets/list.png" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">List of all the courses</h5>
            <p class="card-text">Manage all the courses as a headteacher.</p>
          </div>
          <div class="card-footer">
            <a class="btn btn-primary" href="#/courses/all">Go</a>
          </div>
        </div>
        <div class="card" v-else>
        </div>
        <div class="card" v-if="this.$global.user_role == 'HEADTEACHER'">
          <img class="card-img-top" src="../assets/calendar.png" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Registrations period</h5>
            <p class="card-text">Manage the period during which the registrations are open.</p>
          </div>
          <div class="card-footer">
            <a class="btn btn-primary" href="#/period">Go</a>
          </div>
        </div>
        <div class="card" v-else>
        </div>
        <div class="card">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import moment from 'moment'

  export default {
    name: "HomePage",
    data() {
      return {
        user_role: localStorage.getItem('role'),
        user_username: localStorage.getItem('username'),
        dateDebut: "",
        dateFin: ""
      }
    },
    filters: {
      formatDate: function (value) {
        if (value) {
          return moment(String(value)).format('D MMMM YYYY');
        }
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (this.user_role == 'ADMIN') {
        this.$router.push({path: '/403'});
      } else {
        axios.get('http://localhost:8081/period', {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.dateDebut = response.data.periodeInscriptionComposite.dateDebut;
              this.dateFin = response.data.periodeInscriptionComposite.dateFin;

              var today = moment();
              var start = moment(this.dateDebut);
              var end = moment(this.dateFin);

              if (this.user_role == 'STUDENT')
                if (today.diff(start) < 0 || today.diff(end) > 0)
                  this.$router.push({path: '/close'});
            }
          })
          .catch(e => {
          })

          //warn if student has completed its registrations
          if(this.user_role === 'STUDENT')  {
            axios.get(`http://localhost:8081/users/${this.user_username}/registrations/`, {withCredentials: true})
            .then(response => {
                this.registrationsList = response.data;

                this.checkValidRegistrations();
            })
            .catch(e => {
                console.log(e.message);
            });
          }
      }
    },
    methods: {
        checkValidRegistrations()   {
            this.showError = false;
            this.showInfo = false;

            if(this.registrationsList[this.user_username].length != 5)  {
                this.error = "You still have to complete your registrations.";
                this.showError = true;
            }
        }
    }
  };
</script>

<style scoped>
  .card-deck {
    margin-bottom: 3em;
  }

  .container {
    margin-top: 2em;
  }

  .card img {
    max-width: 50%;
    display: block;
    margin-left: auto;
    margin-right: auto;
    margin-top: 1em;
  }
</style>
