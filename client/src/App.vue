<template>
  <div
    id="app"
    class="min-w-screen min-h-screen bg-gray-100 flex items-center justify-center bg-gray-100 font-sans py-6"
  >
    <div class="container">
      <resource-tester :apiUrl="this.url" v-bind:jwt="this.jwt" />
      <sign-up-form v-bind:apiUrl="this.url" />
      <login-form v-bind:apiUrl="this.url" @Authenticated="authenticated"/>
    </div>
  </div>
</template>

<script>
import ResourceTester from "./components/ResourceTester.vue";
import LoginForm from "./components/LoginForm.vue";
import SignUpForm from "./components/SignUpForm.vue";
export default {
  name: "App",
  components: {
    ResourceTester,
    LoginForm,
    SignUpForm,
  },
  data() {
    return {
      url: "http://localhost:7050",
      jwt: ""
    };
  },
  methods: {
    authenticated: function(event){
      console.log(event);
      this.jwt = event.jwt
      console.log(this.jwt);
    }
  },
};
</script>
