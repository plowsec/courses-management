<template>
  <div class="container" align="center">
    <h2 class="display-4">All Courses List</h2>
    <br/>
    <table class="table table-bordered">
      <thead>
      <tr>
        <th>Title</th>
        <th>Max number of students</th>
        <th>State</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="{coursId, titre, descriptionXML, etudMax, etat} in listCours" :key="coursId">
        <td>
          {{titre}}
        </td>
        <td>
          {{etudMax}}
        </td>
        <td>
          <select v-model="etat">
            <option v-for="state in states">
              {{ state.text }}
            </option>
          </select>
        </td>
      </tr>
      </tbody>
    </table>
    <button v-on:click="update" type="button" class="btn btn-primary">Update</button>
    <button v-on:click="validate" type="button" class="btn btn-success">Validate</button>
  </div>
</template>

<script>
  import axios from 'axios'
  import moment from 'moment'

  export default {
    name: 'coursesList',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        listCours: [],
        tempListCours: [],
        state: "",
        dateDebut: "",
        dateFin: "",
        states: [
          {text: 'Brouillon', value: '1'},
          {text: 'Valide', value: '2'},
          {text: 'Actif', value: '3'}
        ]
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (this.user_role != 'HEADTEACHER') {
        this.$router.push({path: '/403'});
      } else {
        axios.get('http://localhost:8081/courses/all', {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.listCours = response.data
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
            this.listCours = e.message
            this.errors.push(e)
          })
        axios.get('http://localhost:8081/period', {withCredentials: true})
          .then(response => {
            this.dateDebut = response.data.periodeInscriptionComposite.dateDebut;
            this.dateFin = response.data.periodeInscriptionComposite.dateFin;
          })
          .catch(e => {
          })
      }
    },
    methods: {
      update: function (event) {
        var today = moment();
        var start = moment(this.dateDebut);
        var end = moment(this.dateFin);

        var e = document.getElementsByTagName("select");
        for (var i = 0; i < this.listCours.length; i++) {
          var newState = e[i].options[e[i].selectedIndex].value;
          if (newState == "Brouillon") {
            if (today.diff(start) < 0) {
              axios.put(`http://localhost:8443/courses/${this.listCours[i].coursId}/draft`,
                {
                  coursId: this.listCours[i].coursId
                }, {withCredentials: true})
                .then(response => {
                })
                .catch(e => {
                })
            } else {
              alert("Cant'change state to 'Draft' for course " + this.listCours[i].titre + ", wrong period.");
            }
          } else if (newState == "Valide") {
            if (today.diff(start) < 0) {
              axios.put(`http://localhost:8443/courses/${this.listCours[i].coursId}/valid`,
                {
                  coursId: this.listCours[i].coursId
                }, {withCredentials: true})
                .then(response => {
                })
                .catch(e => {
                })
            } else {
              alert("Cant'change state to 'Valid' for course " + this.listCours[i].titre + ", wrong period.");
            }
          } else if (newState == "Actif") {
            if (today.diff(start) > 0 && today.diff(end) < 0) {
              axios.put(`http://localhost:8443/courses/${this.listCours[i].coursId}/active`,
                {
                  coursId: this.listCours[i].coursId
                }, {withCredentials: true})
                .then(response => {
                })
                .catch(e => {
                })
            } else {
              alert("Cant'change state to 'Active' for course " + this.listCours[i].titre + ", wrong period.");
            }
          }
        }
      },
      validate: function (event) {
        this.$modal.show("dialog", {
          title: 'Warning',
          text: 'This action will cause to close the registration period. The courses marked as active will be teached to the registred students. The is no coming back. Are you sure ?',
          buttons: [
            {
              title: 'Close the registration period',
              handler: () => {
                axios.get('http://localhost:8081/courses/end', {withCredentials: true})
                  .then(response => {
                    // Then ...
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
  table tbody tr {
    cursor: default;
  }
</style>
