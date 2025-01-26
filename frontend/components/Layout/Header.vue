<template>
  <div class="h-full">
    <div class="flex h-full justify-between items-center px-6 py-4">
      <button @click="router.push('/')">
        <p>IDS Alimentari</p>
      </button>
      <div>
        <ClientOnly>
          <UButton
            :icon="
              isDark ? 'i-heroicons-moon-20-solid' : 'i-heroicons-sun-20-solid'
            "
            color="gray"
            variant="ghost"
            aria-label="Theme"
            @click="isDark = !isDark"
          />
          <template #fallback>
            <div class="w-8 h-8"></div>
          </template>
        </ClientOnly>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const router = useRouter();
const colorMode = useColorMode();
const isDark = computed({
  get() {
    return colorMode.value === "dark";
  },
  set() {
    colorMode.preference = colorMode.value === "dark" ? "light" : "dark";
  },
});
</script>
