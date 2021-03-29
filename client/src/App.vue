<template>
  <div
    id="app"
    class="min-w-screen min-h-screen bg-gray-100 flex items-center justify-center bg-gray-100 font-sans py-6"
  >
    <div class="container">
      <resource-tester />

      <div class="card bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <div class="title">Sign Up</div>
        <form class="">
          <div class="mb-4">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="username"
            >
              Username
            </label>
            <input
              v-model="signup.username"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="username"
              type="text"
              placeholder="Username"
            />
          </div>
          <div class="mb-6">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="password"
            >
              Password
            </label>
            <input
              v-model="signup.password"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
              id="password"
              type="password"
              placeholder="******************"
            />
          </div>
          <div class="flex items-center justify-between">
            <button
              @click="submitSignUp()"
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="button"
            >
              Submit
            </button>
          </div>
        </form>
      </div>

      <div class="card bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <div class="title">Log In</div>
        <form class="">
          <div class="mb-4">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="username"
            >
              Username
            </label>
            <input
              v-model="login.username"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="username"
              type="text"
              placeholder="Username"
            />
          </div>
          <div class="mb-6">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="password"
            >
              Password
            </label>
            <input
              v-model="login.password"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
              id="password"
              type="password"
              placeholder="******************"
            />
          </div>
          <div class="flex items-center justify-between">
            <button
              @click="submitLogin()"
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="button"
            >
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import "./components/ResourceTester";
import ResourceTester from "./components/ResourceTester.vue";

const url = "http://localhost:7050";

export default {
  name: "App",
  components: {
    ResourceTester,
  },
  data() {
    return {
      signup: {
        username: "",
        password: "",
      },
      login: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    submitSignUp() {
      console.log(JSON.stringify(this.signup));
      fetch(url + "/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username: this.signup.username,
          password: this.signup.password,
        }),
      }).then((response) => {
        console.log(response.status);
      });
    },
    submitLogin() {
      console.log(JSON.stringify(this.login));
      fetch(url + "/users/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username: this.login.username,
          password: this.login.password,
        }),
      }).then((response) => {
        console.log(response.status);
        response.json().then( data => console.log(data))
      });
    },
  },
};
</script>
