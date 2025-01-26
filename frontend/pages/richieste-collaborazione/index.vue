<template>
  <div>
    <div>Richieste Collaborazione</div>
    <div>
      <div class="flex justify-end gap-x-4">
        <UButton to="/richieste-collaborazione/new"
          >Nuova Richiesta di Collaborazione</UButton
        >
        <UButton :loading="loading" @click="getRichiesteCollaborazione">
          Ricarica</UButton
        >
      </div>
      <UTable :loading="loading" :rows="richiesteCollaborazione"></UTable>
    </div>
  </div>
</template>
<script setup lang="ts">
const loading = ref(false);
const richiesteCollaborazione = ref([] as any[]);
const toast = useToast();
const getRichiesteCollaborazione = async () => {
  loading.value = true;
  try {
    const data =
      await useNuxtApp().$api.richieste_collaborazione.getRichiesteCollaborazione();
    loading.value = false;
    richiesteCollaborazione.value = data;
  } catch (error: { response: string } | any) {
    richiesteCollaborazione.value = [];
    loading.value = false;
    toast.add({
      id: "error_get_richieste_collaborazione",
      title: error.response._data.error,
      icon: "i-heroicons-x-circle",
      color: "red",
    });
  }
};
getRichiesteCollaborazione();
</script>
