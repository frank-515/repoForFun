<template>
    <div style="padding-left: 5ex;">
        <div class="head">
            <el-icon>
                <Goods />
            </el-icon>
            Order list
        </div>
        <div class="list">
            <ul :class="i % 2 ? 'item' : ''" v-for="item, i in list" :key="item.valueOf()">
                <thumbnailProductCard style="padding: 1ex;" :id="item.valueOf()"></thumbnailProductCard>
            </ul>
            <a v-if="props.list!.length" style="color: grey;">{{ list!.length }} results</a>

            <div v-else>
                <el-result icon="info" title="There is no more results"></el-result>
            </div>

        </div>
    </div>
</template>

<script setup lang="ts">
import thumbnailProductCard from './thumbnailProductCard.vue';
import { onMounted } from 'vue'

const props = defineProps({
    list: Array<Number>, //UserID
    id: Number
})
props

onMounted(() => {
    fetchInfo()
})

const fetchInfo = () => {

}

</script>

<style scoped>
.head {
    font-size: xx-large;
    font-weight: bolder;
    padding-block: 3ex;
    display: flex;
    align-items: center;
}

.list {}

.item {
    background-color: rgb(240, 247, 249);
    border-radius: 1ex;
}
</style>