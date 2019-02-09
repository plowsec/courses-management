<template>
  <div class="container hello" align="center">
    <h2 class="display-4">Add Course</h2>
    <br/>
    <div class="alert alert-danger" v-if="showError">
      {{error}}
    </div>
    <div class="alert alert-primary" v-if="showInfo">
      {{info}}
    </div>
    <form name="addform" class="formulaire" v-on:submit="postAdd">
      <div class="form-row col-centered">
        <div class="form-group col-md-8">
          <label class="col-md-8 col-form-label">Duplicate existing course</label>
          <select v-model="duplicatedCours" class="form-control" id="inputType">
            <option v-for="cours in listCours" :key="cours.coursId" v-bind:value="cours">{{ cours.titre }}</option>
          </select><br/>
          <button class="btn btn-primary" type="button" v-on:click="duplicateCourse">Duplicate</button>
        </div>
      </div>
      <hr>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label>Title</label>
          <input type="text" v-model="titre" required="required" class="form-control" id="title">
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-3">
          <label class="col-md-3 col-form-label">MaxSttudents</label>
          <input type="text" v-model="etudMax" required="required" class="form-control" id="nbStudent">
        </div>
        <div class="form-group col-md-2">
          <label class="col-md-2 col-form-label">Semester</label>
          <select v-model="semestrePref" class="form-control" id="semester">
            <option v-for="semestre in semesters" v-bind:key="semestre.value">
              {{ semestre.text }}
            </option>
          </select>
        </div>
        <div class="form-group col-md-3">
          <label class="col-md-3 col-form-label">State</label>
          <select v-model="etat" class="form-control" id="state">
            <option v-for="e in etats" v-bind:key="e.value">
              {{ e.text }}
            </option>
          </select>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label class="col-md-8 col-form-label">Implicated teachers</label>
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
            <option v-for="teacher in implicatedTeachers" :key="teacher.userName" v-bind:value="teacher">{{
              teacher.userName }}
            </option>
          </select>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-8">
          <label for="description">Description</label>
          <textarea class="form-control" v-model="description" id="description" rows="3"></textarea>
        </div>
      </div>
      <button type="submit" class="btn btn-primary addButton">Ajouter</button>
    </form>
  </div>
</template>

<script>

  import axios from 'axios'

  export default {
    name: 'AddCourse',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        listCours: [],
        duplicatedCours: '',
        showError: false,
        showInfo: false,
        semesters: [
          {text: 'Automne', value: 'Automne'},
          {text: 'Printemps', value: 'Printemps'}
        ],
        info: '',
        titre: '',
        semestrePref: '',
        description: '',
        etudMax: 0,
        aProf: '',
        teachers: [],
        selectProf: '',
        implicatedTeachers: [],
        etat: '',
        etats: [
          {text: 'Brouillon', value: 'Brouillon'},
          {text: 'Valide', value: 'Valide'},
          {text: 'Actif', value: 'Actif'}
        ]
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (['HEADTEACHER', 'TEACHER'].indexOf(this.user_role) < 0) {
        this.$router.push({path: '/403'});
      } else {
        axios.get('http://localhost:8081/courses', {withCredentials: true})
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
        axios.get('http://localhost:8081/users/teachers/', {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.teachers = response.data
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
            this.teachers = e.message
            this.errors.push(e)
          })
      }
    },
    methods: {
      postAdd(event) {
        axios.post('http://localhost:8081/courses/', {
          titre: this.titre,
          semestrePref: this.semestrePref,
          descriptionXML: this.description,
          etudMax: this.etudMax,
          etat: this.etat,
          profs: this.implicatedTeachers
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
      getxml: function () {
      },
      addProf(event) {
        this.implicatedTeachers.push(this.aProf)
      },
      removeProf() {
        this.implicatedTeachers.pop()
      },
      duplicateCourse(event) {
        axios.get(`http://localhost:8081/courses/${this.duplicatedCours.coursId}/implication`, {withCredentials: true})
          .then(response => {
            this.implicatedTeachers = response.data
          })
          .catch(e => {
            this.implicatedTeachers = e.message
            this.errors.push(e)
          })
        this.titre = this.duplicatedCours.titre
        this.etudMax = this.duplicatedCours.etudMax
        this.semestrePref = this.duplicatedCours.semestrePref
        this.description = this.duplicatedCours.descriptionXML
        this.etat = this.duplicatedCours.etat
      }
    }
  }
</script>
<style lang="css">
  @import '../css/addUser.css';
</style>
