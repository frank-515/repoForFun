<template>
    <div class="outter-wrapper">
        <el-space size="small" wrap>
            <ul v-for="item in list!" :key="item">
                <div @click="onClick(item)">
                    <productCard :id="item"></productCard>
                </div>
                
            </ul>
        </el-space>
    </div>
</template>

<script setup lang="ts">
import productCard from './productCard.vue';

const props = defineProps({
    list: Array<number>
})
props

const onClick = (id: Number) => {
    location.href = '/#/product/' + id
}

</script>

<style scoped>
.outter-wrapper {
    align-items: center;
}
</style>