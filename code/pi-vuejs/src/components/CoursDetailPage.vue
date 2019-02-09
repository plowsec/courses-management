<template>
  <div class="container hello" align="center">
    <h2 class="display-4" >{{titre}}</h2>
    <br/>
    <div class="alert alert-danger" v-if="showError">
      {{error}}
    </div>
    <div class="alert alert-primary" v-if="showInfo">
      {{info}}
    </div>
    <form v-if="isModified()" name="addform" class="formulaire">
      <div class="form-row">
        <div class="form-group col-md-8">
          <label>Title</label>
          <input type="text" v-model="titre" required="required" class="form-control" id="title">
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-3">
          <label class="col-md-4 col-form-label">MaxStudents</label>
          <input type="text" v-model="etudMax" required="required" class="form-control" id="nbStudent">
        </div>
        <div class="form-group col-md-3">
          <label class="col-md-3 col-form-label">Semester</label>
          <select v-model="semestrePref" class="form-control" id="semester">
            <option v-for="semestre in semesters" v-bind:key="semestre.value">
              {{ semestre.text }}
            </option>
          </select>
        </div>
        <div class="form-group col-md-2">
          <label class="col-md-2 col-form-label">State</label>
          <select v-model="etat" class="form-control" id="state">
            <option v-for="e in etatsM" v-bind:key="e.value">
              {{ e.text }}
            </option>
          </select>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label class="col-md-8 col-form-label">Add implicated teachers</label>
          <select v-model="aProf" class="form-control" id="selectedProf">
            <option v-for="teacher in teachers" :key="teacher.userName" v-bind:value="teacher">{{ teacher.userName }}
            </option>
          </select><br/>
          <button class="btn btn-primary" type="button" v-on:click="addProf">Add selected teacher</button>
          <button class="btn btn-primary" type="button" v-on:click="removeProf">Remove teacher</button>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <select class="custom-select" multiple>
            <option v-for="teacher in implicatedTeachers" :key="teacher.userName" v-bind:value="teacher">
              {{teacher.userName }}
            </option>
          </select>
        </div>
      </div>
      <span class="align-left">Registered students:</span>
      <div class="form-row">
        <div class="form-group col-md-8">
          <select class="custom-select" multiple>
            <option v-for="student in registeredStudents" :key="student.fkEleve" v-bind:value="student">
              {{student.fkEleve }}
            </option>
          </select>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label for="descr">Description</label>
          <vue-editor v-model="description"></vue-editor>
        </div>
      </div>
      <button v-on:click="updateCours" class="btn btn-primary addButton">Valider</button>
    </form>
    <div v-else class="container centered">
      <div class="form-row">
        <div class="form-group col-md-2">
          <label class="col-md-3 col-form-label">MaxStudents</label>
          <input type="text" class="form-control centeredText" v-model="etudMax" readonly>
        </div>
        <div class="form-group col-md-3">
          <label class="col-md-3 col-form-label">Semester</label>
          <input type="text" class="form-control centeredText" v-model="semestrePref" readonly>
        </div>
        <div class="form-group col-md-3">
          <label class="col-md-3 col-form-label">State</label>
          <input type="text" class="form-control centeredText" v-model="etat" readonly>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label class="col-md-5">Implicated teachers:</label><label class="col-md-5">Registered students:</label>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <select class="custom-select col-md-5" id="Teachers" multiple>
            <option v-for="teacher in implicatedTeachers" :key="teacher.userName" v-bind:value="teacher">
              {{teacher.userName }}
            </option>
          </select>
          <select class="custom-select col-md-5" id="Students" multiple>
            <option v-for="student in registeredStudents" :key="student.fkEleve" v-bind:value="student">
              {{student.fkEleve }}
            </option>
          </select>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label for="descr">Description</label>
          <!--<textarea class="form-control" v-model="description" id="descr" rows="3" readonly></textarea>-->
          <div v-html="description"></div>
        </div>
      </div>
    </div>
    <a class="btn btn-primary" href="#/courses">Retour</a>
  </div>
</template>

<script>

  import axios from 'axios'

  export default {
    name: 'AddCourse',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        user_name: localStorage.getItem('username'),
        coursID: this.$route.params.id,
        cours: '',
        duplicatedCours: '',
        showError: false,
        showInfo: false,
        semesters: [
          {text: 'Automne', value: 'Autumn'},
          {text: 'Printemps', value: 'Spring'}
        ],
        info: '',
        titre: '',
        semestrePref: '',
        description: '',
        registeredStudents: [],
        etudMax: 0,
        etat: '',
        aProf: '',
        isProf: false,
        teachers: [],
        etatsM: [
          {text: 'Brouillon', value: 'Brouillon'},
          {text: 'Valide', value: 'Valide'}
        ],
        etats: [
          {text: 'Brouillon', value: 'Brouillon'},
          {text: 'Valide', value: 'Valide'},
          {text: 'Actif', value: 'Actif'}
        ],
        selectProf: '',
        implicatedTeachers: []
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (['HEADTEACHER', 'TEACHER', 'STUDENT'].indexOf(this.user_role) < 0) {
        this.$router.push({path: '/403'});
      } else {
        this.isProf =
          this.user_role == "HEADTEACHER" || this.user_role == "TEACHER";
        axios.get(`http://localhost:8081/courses/${this.coursID}`, {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.cours = response.data
              this.titre = this.cours.titre
              this.etudMax = this.cours.etudMax
              this.etat = this.cours.etat
              this.semestrePref = this.cours.semestrePref
              this.description = this.cours.descriptionXML
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
            this.cours = e.message
            this.errors.push(e)
          })
      }
      this.isModified()
      this.loadTeachers()
      this.loadImplicatedTeachers()
      this.loadRegisteredStudents()
    },
    methods: {
      addProf(event) {
        this.implicatedTeachers.push(this.aProf)
      },
      postAdd(event) {
        axios.post('http://localhost:8081/courses/update', {
          oldCours: this.cours,
          coursId: this.coursID,
          titre: this.titre,
          semestrePref: this.semestrePref,
          descriptionXML: this.description,
          etudMax: this.etudMax,
          etat: this.etat,
        }, {withCredentials: true})
          .then(response => {
            this.info = 'L\'ajout s\'est passé correctement'
            this.showInfo = true
          })
          .catch(e => {
            this.error = 'Erreur lors de l\'ajout : ' + e.message
            this.showError = true
          })
      },
      updateCours() {
        axios.post('http://localhost:8081/courses/update', {
          oldCours: this.cours,
          coursId: this.coursID,
          titre: this.titre,
          semestrePref: this.semestrePref,
          descriptionXML: this.description,
          etudMax: this.etudMax,
          etat: this.etat,
          profs:this.implicatedTeachers
        }, {withCredentials: true})
          .then(response => {
            this.info = 'L\'update s\'est passé correctement'
            this.showInfo = true
          })
          .catch(e => {
            this.error = 'Erreur lors de l\'update : the descrition was already modified : ' + e.message
            this.showError = true
          })
      },
      loadTeachers() {
        if(!this.isProf) return;
        axios.get('http://localhost:8081/users/teachers/', {withCredentials: true})
          .then(response => {
            this.teachers = response.data
          })
          .catch(e => {
            this.teachers = e.message
            this.errors.push(e)
          })
      },
      isModified(){
        var implicated = false
        for(var i = 0; i < this.implicatedTeachers.length; i++){
          if(this.user_name === this.implicatedTeachers[i].userName)
            implicated = true
        }
        return (this.etat !== 'Actif' && this.isProf && implicated)
      },
      loadImplicatedTeachers() {
        axios.get(`http://localhost:8081/courses/${this.coursID}/implication`, {withCredentials: true})
          .then(response => {
            this.implicatedTeachers = response.data
          })
          .catch(e => {
            this.implicatedTeachers = e.message
            this.errors.push(e)
          })
      },
      loadRegisteredStudents(){
        axios.get(`http://localhost:8081/courses/${this.coursID}/registrations`, {withCredentials: true})
          .then(response => {
            this.registeredStudents = response.data
          })
          .catch(e => {
            this.registeredStudents = e.message
            this.errors.push(e)
          })
      },
      removeProf() {
        this.implicatedTeachers.pop()
      }
    }
  }
</script>
<style lang="css">
  @import '../css/addUser.css';
</style>
