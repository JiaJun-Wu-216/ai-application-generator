<template>
  <div id="userLoginPage">
    <h2 class="title">AI 应用生成平台 - 用户登录</h2>
    <div class="desc">不写一行代码，生成完整应用</div>
    <a-form :model="formState" name="basic" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input
          v-model:value="formState.userAccount"
          placeholder="请输入账号"
          autocomplete="username"
        />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码不能小于 8 位' },
        ]"
      >
        <a-input-password
          v-model:value="formState.userPassword"
          placeholder="请输入密码"
          autocomplete="current-password"
        />
      </a-form-item>
      <div class="tips">
        没有账号？
        <RouterLink to="/user/register">去注册</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {reactive} from 'vue'
import type {UserLoginRequest} from '@/entity/User.ts'
import {useRouter} from 'vue-router'
import {useLoginUserStore} from '@/stores/LoginUser.ts'
import {login} from '@/api/UserController.ts'
import {message} from 'ant-design-vue'

const formState = reactive<UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const router = useRouter()
const loginUserStore = useLoginUserStore()

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: UserLoginRequest) => {
  const response = await login(values)
  // 登录成功，把登录态保存到全局状态中
  if (response.code === 0 && response.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    await router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败，' + response.message)
  }
}
</script>

<style scoped>
#userLoginPage {
  max-width: 360px;
  padding: 10px;
  margin: 20vh auto 0;
  background: #f6f4f4;
  border-radius: 16px;
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.1),
    0 8px 24px rgba(0, 0, 0, 0.15);
}

.title {
  text-align: center;
  margin-top: 16px;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}

.tips {
  margin-bottom: 8px;
  color: #bbb;
  font-size: 13px;
  text-align: right;
}
</style>
