<template>
  <div class="login-container">
    <div class="card bg-white shadow-md rounded px-9 pt-6 pb-8 mb-4">
      <div class="title">Log In</div>
      <form class="">
        <div class="mb-5">
          <label
            class="block text-gray-701 text-sm font-bold mb-2"
            for="username"
          >
            Username
          </label>
          <input
            v-model="username"
            class="shadow appearance-none border rounded w-full py-3 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="username"
            type="text"
            placeholder="Username"
          />
        </div>
        <div class="mb-7">
          <label
            class="block text-gray-701 text-sm font-bold mb-2"
            for="password"
          >
            Password
          </label>
          <input
            v-model="password"
            class="shadow appearance-none border rounded w-full py-3 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
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
</template>

<script>
export default {
  props: {
    apiUrl: String,
    jwt: String
  },
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    submitLogin() {
      fetch(this.apiUrl + "/users/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
        }),
      }).then((response) => {
        response.json().then((data) => {
          console.log(data)
          this.$emit("Authenticated", {"jwt": data["jwt"]})
          });
      });
    },
  },
};
</script>