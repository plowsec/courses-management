<template>
  <div class="container" align="center">
    <h2 class="display-4">Period</h2>
    <br/>
    <p>Current period starts {{currentDateDebut | formatDate}} and ends {{currentDateFin | formatDate}}.</p>
    <br/>
    <div class="row">
      <div class="col-2"></div>
      <div class="col-4">
        <p>Start date: {{newDateDebut | formatDate}}</p>
        <datepicker :inline="true" v-model="newDateDebut"></datepicker>
      </div>
      <div class="col-4">
        <p>End date: {{newDateFin | formatDate}}</p>
        <datepicker :inline="true" v-model="newDateFin" class="disabled"></datepicker>
      </div>
      <div class="col-2"></div>
    </div>
    <br/>
    <button v-on:click="update" type="button" class="btn btn-primary">Update</button>
  </div>
</template>

<script>
  import axios from 'axios'
  import Datepicker from 'vuejs-datepicker'
  import moment from 'moment'

  export default {
    name: 'Period',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        currentDateDebut: "",
        currentDateFin: "",
        tempCurrentDateDebut: "",
        tempCurrentDateFin: "",
        newDateDebut: "",
        newDateFin: "",
        isPeriodOK: false
      }
    },
    components: {
      Datepicker
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
      } else if (this.user_role != 'HEADTEACHER') {
        this.$router.push({path: '/403'});
      } else {
        axios.get('http://localhost:8081/period', {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.currentDateDebut = response.data.periodeInscriptionComposite.dateDebut;
              this.newDateDebut = this.currentDateDebut;
              this.currentDateFin = response.data.periodeInscriptionComposite.dateFin;
              this.newDateFin = this.currentDateFin;

              var today = moment();
              var start = moment(this.newDateDebut);

              if (today.diff(start) < 0)
                this.isPeriodOK = true;
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
          })
      }
    },
    methods: {
      update: function (event) {
        if (this.isPeriodOK) {
          var start = moment(this.newDateDebut);
          var end = moment(this.newDateFin);
          if (start.diff(end) < 0) {
            this.isPeriodOK = true;
            axios.get('http://localhost:8081/period', {withCredentials: true})
              .then(response => {
                this.tempCurrentDateDebut = response.data.periodeInscriptionComposite.dateDebut;
                this.tempCurrentDateFin = response.data.periodeInscriptionComposite.dateFin;
                if (this.tempCurrentDateDebut == this.currentDateDebut && this.tempCurrentDateFin == this.currentDateFin) {
                  var obj = '{ "dateDebut": "' + moment(String(this.newDateDebut)).format('YYYY-MM-DD') + '", "dateFin": "' + moment(String(this.newDateFin)).format('YYYY-MM-DD') + '" }';
                  axios.put('http://localhost:8081/period', {
                    periodeInscriptionComposite: JSON.parse(obj)
                  }, {withCredentials: true})
                    .then(response => {
                      alert("[OK] Period updated.");
                      this.currentDateDebut = this.newDateDebut;
                      this.currentDateFin = this.newDateFin;
                    })
                    .catch(e => {
                      alert("[NOK] Problem while updating the period.");
                    })

                } else {
                  alert("[NOK] Problem while updating the period.");
                }
              })
              .catch(e => {
                alert(e.message);
              })
          } else {
            alert("[NOK] Start date must be before end date.");
          }
        } else {
          alert("[NOK] Not possible to update the period anymore.");
        }
      }
    }
  }
</script>
