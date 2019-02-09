<template>
  <div class="container" align="center">
    <h2 class="display-4">Summary table</h2>
    <br/>
    <div class="table-wrapper" style="overflow:auto;">
      <table class="table table-bordered">
        <thead>
        <tr>
          <th></th>
          <th v-for="cours in summaryData['courses']" :key="cours.coursId">
            {{cours.titre}}
          </th>
          <th>Total</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in Object.keys(summaryData['regs'])" :key="student">
          <td>{{student}}</td>
          <td v-for="priorite in Object.keys(regs[student])" :key="priorite">{{ regs[student][priorite] }}</td>
          <td class="table-success" v-if="summaryData['regs'][student].length == 5">
            {{summaryData['regs'][student].length}}
          </td>
          <td class="table-danger" v-else>{{summaryData['regs'][student].length}}</td>
        </tr>
        <tr>
          <td>Total</td>
          <td v-for="cours in Object.keys(coursesCount)" :key="cours">
            {{coursesCount[cours]}}
          </td>
          <td>{{ totalRegistrations }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <a class="btn btn-primary" id="show-modal" v-on:click="exportDialog()">Export</a>
    <!--<v-dialog name="dialog-3" />-->
  </div>
</template>


<script>
  import jsPDF from 'jspdf'
  import axios from 'axios'

  export default {
    name: 'SummaryPage',
    data() {
      return {
        user_role: localStorage.getItem('role'),
        summaryData: [],
        studentsRegistrations: [],
        regs: {},
        coursesCount: {},
        totalRegistrations: 0,
        showModal: 0
      }
    },
    created() {
      if (!this.user_role) {
        this.$router.push({path: '/'});
      } else if (['HEADTEACHER', 'TEACHER'].indexOf(this.user_role) > -1) {
        axios.get('http://localhost:8081/summary', {withCredentials: true})
          .then(response => {
            if (response.status == '200') {
              this.summaryData = response.data
              this.regs = {};
              this.coursesCount = {};
              this.populateRegs();
              this.totalRegistrations;
              this.showModal = false;
              this.getTotal();
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
            //this.summaryData = e.message
            console.log("errors:" + e.message);
            //this.errors.push(e)
          })
      } else {
        this.$router.push({path: '/403'});
      }
    },
    methods: {
      exportDialog() {
        this.$modal.show("dialog", {
          title: 'Export summary',
          text: 'Choose an export format',
          buttons: [
            {
              title: 'CSV',
              handler: () => {
                this.exportCSV()
              }
            },
            {
              title: 'PDF',       // Button title
              default: true,    // Will be triggered by default if 'Enter' pressed.
              handler: () => {
                this.exportPDF()
              } // Button click handler
            },
            {
              title: 'Cancel'
            }
          ]
        })
      },
      getTableData() {
        var column_names = [""];
        for (var i = 0; i < this.summaryData['courses'].length; i++) {
          column_names.push(this.summaryData['courses'][i].titre);
        }
        column_names.push("Total");

        var rows = [];
        for (var student in this.summaryData['regs']) {
          var row = [student];
          for (var key in this.regs[student]) {
            //var id = Object.keys(this.regs[student])[j];
            row.push(this.regs[student][key]);
          }
          row.push(this.summaryData['regs'][student].length + "");
          rows.push(row);
        }

        var totalRow = ["Total"];
        var ckeys = Object.keys(this.coursesCount);
        for (var z = 0; z < ckeys.length; z++) {
          var tot = parseInt(this.coursesCount[ckeys[z]]);
          if (isNaN(tot))
            tot = 0;
          totalRow.push(tot);
        }
        totalRow.push(this.totalRegistrations);
        rows.push(totalRow);

        var packed = [column_names, rows];
        return packed;
      },
      exportPDF() {

        var packed = this.getTableData();
        var column_names = packed[0];
        var rows = packed[1];

        //generate pdf
        var doc = new jsPDF({
          orientation: 'landscape',
          unit: 'in',
          format: [20, 10]
        });
        doc.autoTable(column_names, rows);
        doc.save('table.pdf');
      },
      exportCSV() {
        var packed = this.getTableData();
        var column_names = packed[0];
        var rows = packed[1];

        let csvContent = "data:text/csv;charset=utf-8,";
        csvContent += column_names.join(",") + "\r\n";
        rows.forEach(function (rowArray) {
          let row = rowArray.join(",");
          csvContent += row + "\r\n";
        });

        var data = encodeURI(csvContent);

        var link = document.createElement('a');
        link.setAttribute('href', data);
        link.setAttribute('download', "table.csv");
        document.body.appendChild(link); // Required for FF
        link.click();
      },
      updateCoursesCount(coursId) {
        if (coursId in this.coursesCount)
          this.coursesCount[coursId]++;
        else
          this.coursesCount[coursId] = 0;
      },
      getTotal() {
        var ckeys = Object.keys(this.coursesCount);

        for (var z = 0; z < ckeys.length; z++) {
          var tot = parseInt(this.coursesCount[ckeys[z]]);
          if (isNaN(tot))
            tot = 0;
          this.totalRegistrations += tot;
        }
        return this.totalRegistrations;
      },
      populateRegs() {

        var students = Object.keys(this.summaryData['regs']);

        for (var i = 0; i < this.summaryData['courses'].length; i++) {
          this.coursesCount[this.summaryData['courses'][i].titre] = 0;
        }

        for (var ii = 0; ii < students.length; ii++) {
          var courses = [];

          for (var i = 0; i < this.summaryData['courses'].length; i++) {
            var id = this.summaryData['courses'][i].titre
            courses[id] = 0;
          }

          var studentRegs = this.summaryData['regs'][students[ii]];
          for (var j = 0; j < studentRegs.length; j++) {
            var currentCourseId = studentRegs[j].cours.titre;
            if (currentCourseId in courses) {
              courses[currentCourseId] = studentRegs[j].priorite;
              this.updateCoursesCount(currentCourseId)
            }
          }

          this.regs[students[ii]] = courses;
        }
      }
    }
  }

</script>

<style scoped>
  h1 {
    margin-bottom: 1em;
  }

  a {
    color: white;
  }

  table tbody tr {
    cursor: default;
  }

  .table-wrapper {
    margin-bottom: 1em;
  }
</style>
