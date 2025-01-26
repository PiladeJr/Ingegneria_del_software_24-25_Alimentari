<template>
  <div class="flex flex-col items-center">
    <label
      for="file"
      class="cursor-pointer bg-gray-100 hover:bg-gray-200 border border-gray-300 text-gray-800 px-4 py-2 rounded-md shadow-sm text-sm font-medium focus:ring-2 focus:ring-blue-500 focus:outline-none"
    >
      {{ fileName || "Seleziona un file..." }}
      <input id="file" type="file" class="hidden" @change="handleFileChange" />
    </label>
    <p v-if="fileName" class="mt-2 text-gray-600 text-sm">
      {{ fileName }}
    </p>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  (e: "file-selected", file: File): void;
}>();

const fileName = ref<string | null>(null);

function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  const file = target.files ? target.files[0] : null;

  if (file) {
    fileName.value = file.name;
    emit("file-selected", file);
  }
}
</script>

<style scoped>
/* Aggiungi eventuali stili personalizzati se necessario */
</style>
