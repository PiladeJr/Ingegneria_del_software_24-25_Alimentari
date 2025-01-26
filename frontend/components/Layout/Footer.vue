<template>
  <div class="flex items-center justify-center">
    <div class="flex items-center gap-x-4">
      <p class="text-sm text-gray-400">
        &copy; {{ currentYear }} IDS Alimentari.
      </p>
      <button
        class="flex items-center"
        @click="
          openLinkNewTab(
            'https://github.com/PiladeJr/Ingegneria_del_software_24-25_Alimentari'
          )
        "
      >
        <UIcon name="i-mdi-github" class="text-xl" />
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
const router = useRouter();
const currentYear = ref(new Date().getFullYear());
const openLinkNewTab = (link: string) => {
  window.open(link, "_blank");
};
</script>
