<template>
  <div class="flex flex-col h-screen">
    <div class="flex-none">
      <LayoutHeader></LayoutHeader>
    </div>
    <div
      class="h-full flex flex-col flex-grow w-full justify-center items-center"
    >
      <slot />
    </div>
    <div class="flex-none">
      <LayoutFooter></LayoutFooter>
    </div>
  </div>
</template>
