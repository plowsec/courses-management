<template>
  <div class="container hello" align="center">
    <h2 class="display-4">New User</h2>
    <br/>
    <div class="alert alert-danger" v-if="showError">
      {{error}}
    </div>
    <div class="alert alert-primary" v-if="showInfo">
      {{info}}
    </div>
    <form name="addform" class="formulaire" v-on:submit="postAdd">
      <div class="form-row">
        <div class="form-group col-md-4">
          <label for="inputUsername">Firstname</label>
          <input type="text" v-model="prenom" required="required" class="form-control" id="inputFirstName">
        </div>
        <div class="form-group col-md-4">
          <label for="inputUsername">Lastname</label>
          <input type="text" v-model="nom" required="required" class="form-control" id="inputLastName">
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-3">
          <label for="inputUsername">Street name</label>
          <input type="text" v-model="rue" required="required" class="form-control" id="inputRoad">
        </div>
        <div class="form-group col-md-1">
          <label for="inputUsername">N°</label>
          <input type="text" v-model="numero" required="required" class="form-control" id="inputNum">
        </div>
        <div class="form-group col-md-1">
          <label for="inputUsername">NPA</label>
          <input type="text" v-model="npa" required="required" class="form-control" id="inputNpa">
        </div>
        <div class="form-group col-md-3">
          <label for="inputUsername">City</label>
          <input type="text" v-model="ville" required="required" class="form-control" id="inputCity">
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-3">
          <label for="inputUsername">Username</label>
          <input type="text" v-model="nomUtilisateur" required="required" class="form-control" id="inputUsername">
        </div>
        <div class="form-group col-md-3">
          <label for="inputUsername">Password</label>
          <input type="password" v-model="motDePasse" required="required" class="form-control" id="inputPassword">
        </div>
        <div class="form-group col-md-2">
          <label for="inputType">Role</label>
          <select v-model="typeCompte" required="required" class="form-control" id="inputType">
            <option v-for="{text, value} in optionsType" :key="text" v-bind:value="value">{{ text }}</option>
          </select>
        </div>
      </div>
      <button type="submit" class="btn btn-primary addButton">Add</button>
    </form>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'AddUser',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        showError: false,
        showInfo: false,
        info: '',
        nomUtilisateur: '',
        prenom: '',
        nom: '',
        motDePasse: '',
        error: '',
        rue: '',
        numero: '',
        npa: '',
        ville: '',
        typeCompte: '',
        optionsType: [
          {text: 'Eleve', value: 'STUDENT'},
          {text: 'Professeur', value: 'TEACHER'},
          {text: 'Admin', value: 'ADMIN'},
          {text: 'Chef filiere', value: 'HEADTEACHER'}
        ]
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (this.user_role != 'ADMIN') {
        this.$router.push({path: '/403'});
      }
    },
    // Définissez les méthodes de l'objet
    methods: {
      postAdd(event) {
        var temp = this.nomUtilisateur.match(/^[a-z]{1,8}\.{1}[a-z]{1,8}$/i)
        if (temp) {
          this.nomUtilisateur = temp.toString()
          this.showError = false
        } else {
          this.showError = true
          this.error = 'Le nom d\'utilisateur doit être composé de maximum 8 caractères suivis d\'un point puis à nouveau 8 caractères max. Exemple: elon.musk'
          return false
        }
        axios.post('http://localhost:8081/users/', {
          userName: this.nomUtilisateur,
          lastName: this.prenom,
          firstName: this.nom,
          password: this.motDePasse,
          role: this.typeCompte,
          adresse: '<adresse><rue num="' + this.numero + '">' + this.rue + '</rue> <ville npa="' + this.npa + '">' + this.ville + '</ville></adresse>'
        },{withCredentials: true})
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
      }
    }
  }
</script>

<style lang="css">
  @import '../css/addUser.css';
</style>
