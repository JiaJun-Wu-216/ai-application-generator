<template>
  <a-config-provider :locale="zhCN">
    <BasicLayout />
  </a-config-provider>
</template>

<script setup lang="ts">
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import BasicLayout from '@/layouts/BasicLayout.vue'
import {onMounted} from 'vue'
import {healCheck} from '@/api/HealthController.ts'

dayjs.locale('zh-cn')

onMounted(async () => {
  console.log(await healCheck())
})
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
}
</style>
