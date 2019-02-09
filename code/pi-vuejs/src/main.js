/* eslint-disable no-unused-vars */
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'

import router from './router'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VModal from 'vue-js-modal'
import jsPDF from 'jspdf'
import 'jspdf-autotable'
import VueEditor from 'vue2-editor'

Vue.use(BootstrapVue);
Vue.use(VModal, {dialog: true});
Vue.use(jsPDF);
Vue.use(VueEditor)

Vue.config.productionTip = false;

var shared = new Vue({
  data: {
    user_role: null,
    user_firstname: null,
    user_lastname: null,
    user_username: null
  }
});

shared.install = function () {
  Object.defineProperty(Vue.prototype, '$global', {
    get() {
      return shared
    }
  })
};
Vue.use(shared);

new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
});


