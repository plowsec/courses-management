<template>
  <div class="container" align="center">
    <h2 class="display-4">Courses List</h2>
    <br/>
    <div class="alert alert-danger" v-if="showError">
      {{error}}
    </div>
    <div class="alert alert-primary" v-if="showInfo">
      {{info}}
    </div>
    <table class="table table-bordered table-hover">
      <thead>
      <tr>
        <th>Title</th>
        <th data-toggle="tooltip" data-placement="right" title="Number of registered students for this course">Number of registrations</th>
        <th v-if="isProf">State</th>
        <th v-else data-toggle="tooltip" data-placement="right" title="Four registrations with priority 1 and one registration with priority 2 are required">Priority</th>
      </tr>
      </thead>
      <tbody>
      <tr v-if="isProf" class="table-info" v-for="draft in listDrafts" :key="draft.coursId">
        <td v-on:click="seeCourseDetails(draft)">
          {{draft.titre}}
        </td>

        <td>
          {{coursesCount[draft.coursId]}}
        </td>
        <td>
          {{draft.etat}}
        </td>
      </tr>
      <tr v-if="isProf" class="table-success" v-for="cours in listOthers" :key="cours.coursId">
        <td v-on:click="seeCourseDetails(cours)">
          {{cours.titre}}
        </td>

        <td>
          {{coursesCount[cours.coursId]}}
        </td>
        <td>
          {{cours.etat}}
        </td>
      </tr>
      <tr v-if="isProf" v-for="cours in listCours" :key="cours.coursId" v-on:click="seeCourseDetails(cours)">
        <td>
          {{cours.titre}}
        </td>

        <td v-if="coursesCount[cours.coursId]">{{coursesCount[cours.coursId]}}</td>
        <td v-else>0</td>
        <td>
          <!--<select v-model="cours.etat">
            <option v-for="etat in etats" v-bind:key="etat.text">
              {{ etat.text }}
            </option>
          </select>-->
          {{cours.etat}}
        </td>
      </tr>
      <tr v-if="!isProf" v-for="cours in listCours" :key="cours.coursId">
        <td v-on:click="seeCourseDetails(cours)">{{cours.titre}}</td>
        <td v-if="coursesCount[cours.coursId]" v-on:click="seeCourseDetails(cours)">{{coursesCount[cours.coursId]}}</td>
        <td v-else v-on:click="seeCourseDetails(cours)">0</td>
        <td v-if="cours.etat == 'Valide'">
          <select :id="prefix+cours.coursId">
            <option>Not registered</option>
            <option>1</option>
            <option>2</option>
          </select>
        </td>
        <td v-else>
          <select disabled :id="prefix+cours.coursId">
            <option>Not registered</option>
            <option>1</option>
            <option>2</option>
          </select>
        </td>
      </tr>
      </tbody>
    </table>
    <span v-if="isProf"><a class="btn btn-primary" href="#/course/create">Add course</a></span>
    <span v-else><button v-on:click="updateInscription" type="button"
                         class="btn btn-primary">Update registrations</button></span>
  </div>
</template>

<script>
  import axios from "axios";

  export default {
    name: "CoursesList",
    data() {
      return {
        user_role: localStorage.getItem("role"),
        user_username: localStorage.getItem("username"),
        showError: false,
        showInfo: false,
        error: '',
        listCours: [],
        registrationList: [],
        listCoursOfTeacher: [],
        listDrafts: [],
        listOthers:[],
        isProf: true,
        priorities: [],
        prefix: "select",
        summaryData: [],
        regs: {},
        coursesCount: {},
        etats: [
          {text: "Brouillon", value: "Brouillon"},
          {text: "Valide", value: "Valide"}
        ]
      };
    },
    created() {

      if (!this.user_role) {
        this.$router.push({path: "/"});
      } else if (
        ["HEADTEACHER", "TEACHER", "STUDENT"].indexOf(this.user_role) < 0
      ) {
        this.$router.push({path: "/403"});
      } else {
        this.isProf =
          this.user_role == "HEADTEACHER" || this.user_role == "TEACHER";
        //get the courses list
        if (this.isProf) {
          axios.get(
            `http://localhost:8081/users/${this.user_username}/courses/`,
            {withCredentials: true}
          )
            .then(response => {
              this.listCoursOfTeacher = response.data;
              this.updateCoursesViewTeacher();
            })
            .catch(e => {
              console.log(e.message)
            });
        }
        axios
          .get("http://localhost:8081/courses/", {withCredentials: true})
          .then(response => {
            this.listCours = response.data;
            if(this.isProf) {
                var tmpListCours = response.data;
                var c = tmpListCours.filter( ( el ) => !this.listCoursOfTeacher.some(function(o2)   {
                    return el.coursId === o2.coursId;
                }) );
                this.listCours = c;
            }
            else{
                 this.listCours = response.data;
            }
          })
          .catch(e => {
            console.log(e.message)
          });

        if (!this.isProf) {
          // then update with the registrations
          axios.get(
            `http://localhost:8081/users/${this.user_username}/registrations/`,
            {withCredentials: true}
          )
            .then(response => {
              this.registrationList = response.data;
              this.updateCoursesView();
            })
            .catch(e => {
              console.log(e.message)
            });
        }

        // then update with the registrations
        axios.get(`http://localhost:8081/registrations/count`, {withCredentials: true})
          .then(response => {
            this.coursesCount = response.data;
          })
          .catch(e => {
            console.log(e.message)
          });
      }
    },
    methods: {
      updateCoursesViewTeacher() {
        for (var i = 0; i < this.listCoursOfTeacher.length; i++) {
          if (this.listCoursOfTeacher[i].etat == "Brouillon") {
            this.listDrafts.push(this.listCoursOfTeacher[i]);
          }
          else  {
              this.listOthers.push(this.listCoursOfTeacher[i]);
          }
        }
      },
      updateCoursesView() {
        var regs = this.registrationList[this.user_username];
        for (var i = 0; i < regs.length; i++) {
          var coursId = regs[i].cours.coursId;
          var priority = regs[i].priorite;
          var id = this.prefix + coursId + "";
          document.getElementById(id).value = priority;
        }
      },
      getRegistrations() {
        var regs = [];
        for (var i = 0; i < this.listCours.length; i++) {
          var id = this.prefix + this.listCours[i].coursId + "";
          var priority = document.getElementById(id).value;
          if (isNaN(priority)) {
            priority = 0;
          }
          var registration = {
            inscriptionComposite: {
              fkEleve: this.user_username,
              fkCours: this.listCours[i].coursId
            },
            priorite: priority
          };
          regs.push(registration);
        }

        return regs;
      },
      isValidRegs(regs) {
        var nbpriority1 = 0;
        var nbpriority2 = 0;
        for (var i = 0; i < regs.length; i++) {
          if (regs[i].priorite == '1')
            nbpriority1++;
          if (regs[i].priorite == '2')
            nbpriority2++;
        }
        return (nbpriority1 == 4 && nbpriority2 == 1);
      },
      updateInscription: function (event) {
        var regs = this.getRegistrations();
        this.showError = false;
        this.showInfo = false;

        //4 registrations with priority 1, 1 reg with priority 2
        if (!this.isValidRegs(regs)) {
          this.error = "Four registrations with priority 1 and one registration with priority 2 are required.";
          this.showError = true;
          return void(0);
        }

        axios
          .put(
            `http://localhost:8081/users/${this.user_username}/registrations`,
            JSON.stringify(regs),
            {
              withCredentials: true,
              headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
              }
            }
          )
          .then(response => {
            this.info = "Update successful";
            this.showInfo = true;
          })
          .catch(e => {
            this.error = e.response.data.message;
            this.showError = true;
          });
      },
      seeCourseDetails(cours) {
        this.$router.push({path: `/course/${cours.coursId}`});
        return false;
      }
    }
  };
</script>

<style scoped>
  table tbody tr {
    cursor: pointer;
  }
</style>
