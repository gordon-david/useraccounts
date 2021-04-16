<template>
  <div class="card bg-white py-3 px-5 rounded flex flex-col mb-5">
    <div class="w-full">
      <div
        v-bind:class="[homeClass]"
        class="py-3 px-5 mb-4 text-sm rounded-md border"
      >
        <p><strong> Home Resource: </strong> Not Authenticated</p>
        <button @click="fetchHome()" class="bg-transparent hover:bg-grey text-grey-dark font-semibold hover:text-white py-2 px-4 border border-grey hover:border-transparent rounded mr-2">Access</button>
      </div>
      <div
        v-bind:class="[userClass]"
        class="py-3 px-5 mb-4 text-sm rounded-md border"
      >
        <p><strong>User Resource:</strong> Requires Role "User"</p>
        <button @click="fetchUser()">Access</button>
      </div>
      <div
        v-bind:class="[adminClass]"
        class="py-3 px-5 mb-4 text-sm rounded-md border"
      >
        <p><strong>Admin Resource:</strong> Requires Role "Admin"</p>
        <button @click="fetchAdmin()">Access</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props:{
    jwt: String
  },
  data: function () {
    return {
      nominal: "bg-blue-100 text-blue-900 border-blue-200",
      success: "bg-green-100 text-green-900 border-green-200",
      error: "bg-red-100 text-red-900 border-red-200",
      homeClass: "",
      userClass: "",
      adminClass: "",
    };
  },
  mounted() {
    this.homeClass = this.nominal;
    this.userClass = this.nominal;
    this.adminClass = this.nominal;
  },
  methods: {
    fetchAdmin() {
      fetch("http://localhost:7050/admintestresource")
        .then((response) => {
          if (!response.ok) {
            this.adminClass = this.error;
            return;
          }
          this.adminClass = this.success;
        })
        .catch((e) => {
          console.error(e);
          this.adminClass = this.error;
        });
    },
    fetchUser() {
      console.log("ping");
      fetch("http://localhost:7050/usertestresource", {
        headers: {
          "Authorization": "Bearer " + this.jwt
        }
      })
        .then((response) => {
          if (!response.ok) {
            this.userClass = this.error;
            return;
          }
          this.userClass = this.success;
        })
        .catch((e) => {
          console.error(e);
          this.userClass = this.error;
        });
    },
    fetchHome() {
      fetch("http://localhost:7050/hometestresource")
        .then((response) => {
          if (!response.ok) {
            this.homeClass = this.error;
            return;
          }
          this.homeClass = this.success;
        })
        .catch((e) => {
          console.error(e);
          this.homeClass = this.error;
        });
    },
  },
};
</script>