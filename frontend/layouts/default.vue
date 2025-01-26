<template>
  <div class="flex flex-col min-h-screen h-screen">
    <div class="flex-none">
      <LayoutHeader></LayoutHeader>
    </div>
    <div class="flex-auto py-4 px-6"><slot /></div>
    <div class="flex-none py-4 px-6">
      <LayoutFooter></LayoutFooter>
    </div>
  </div>
</template>
