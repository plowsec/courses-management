import Vue from "vue";
import Router from "vue-router";
import LoginPage from "@/components/LoginPage";
import HomePage from "@/components/HomePage";
import AddUserPage from "@/components/AddUserPage";
import PeriodPage from "@/components/PeriodPage";
import AllCoursesPage from "@/components/AllCoursesPage";
import CoursesList from "@/components/CoursesListPage";
import StudentRegistrations from "@/components/StudentRegistrations";
import AddCourse from "@/components/AddCoursePage";
import SummaryPage from "@/components/SummaryPage";
import ListEleves from "@/components/ListEleves";
import CoursDetail from "@/components/CoursDetailPage";
import HTTP400 from "@/components/ErrorPages/HTTP400";
import HTTP401 from "@/components/ErrorPages/HTTP401";
import HTTP403 from "@/components/ErrorPages/HTTP403";
import HTTP500 from "@/components/ErrorPages/HTTP500";
import RegistrationsClosed from "@/components/ErrorPages/RegistrationsClosed";
Vue.use(Router);
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
export default new Router({
  routes: [
    {
      path: "/",
      name: "Login",
      component: LoginPage
    },
    {
      path: "/home",
      name: "Home",
      component: HomePage
    },
    {
      path: "/user/create",
      name: "AddUser",
      component: AddUserPage
    },
    {
      path: "/period",
      name: "Period",
      component: PeriodPage
    },
    {
      path: "/courses/all",
      name: "AllCourses",
      component: AllCoursesPage
    },
    {
      path: "/courses",
      name: "CoursesList",
      component: CoursesList
    },
    {
      path: "/students",
      name: "StudentsList",
      component: ListEleves
    },
    {
      path: "/student/:id",
      name: "Registrations",
      component: StudentRegistrations
    }, {
      path: "/course/create",
      name: "courseAdd",
      component: AddCourse
    },
    {
      path: "/course/:id",
      name: "CoursDetail",
      component: CoursDetail
    },
    {
      path: "/summary",
      name: "Summary",
      component: SummaryPage
    },
    {
      path: "/course/create",
      name: "AddCourse",
      component: AddCourse
    },
    {
      path: "/400",
      name: "HTTP400",
      component: HTTP400
    },
    {
      path: "/401",
      name: "HTTP401",
      component: HTTP401
    },
    {
      path: "/403",
      name: "HTTP403",
      component: HTTP403
    },
    {
      path: "/500",
      name: "HTTP500",
      component: HTTP500
    },
    {
      path: "/close",
      name: "RegistrationsClosed",
      component: RegistrationsClosed
    }

  ]
});
