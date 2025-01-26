// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  modules: ["@nuxt/ui", "@pinia/nuxt"],
  components: [
    {
      path: "~/components",
      priority: 2,
    },
    {
      path: "~/components/common",
      pathPrefix: false,
    },
  ],
  runtimeConfig: {
    public: {
      ENV: process.env.ENV,
      API_BASE_URL: process.env.API_BASE_URL || "http://localhost:8080",
      SSR_API_BASE: process.env.SSR_BASE_URL,
      ECHO_AUTH_URL: "localhost",
    },
  },
});
