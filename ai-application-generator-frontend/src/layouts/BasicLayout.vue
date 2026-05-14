<template>
  <a-layout class="basic-layout">
    <GlobalHeader />
    <a-layout-content class="layout-content">
      <router-view />
    </a-layout-content>
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '../components/GlobalHeader.vue'
import GlobalFooter from '../components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  min-height: 100vh;
}

.layout-content {
  padding: 24px;
  background: #f5f5f5;
}

@media (max-width: 768px) {
  .layout-content {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .layout-content {
    padding: 12px;
  }
}
</style>
