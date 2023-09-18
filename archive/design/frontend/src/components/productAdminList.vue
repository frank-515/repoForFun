<template>

<el-table :data="list" stripe style="width: 100%">
  
    <el-table-column prop="product_id" label="ID" width="80" />
    <el-table-column prop="product_name" label="Name" width="180" />
    <el-table-column prop="product_quantity" label="Stock" width="80" />
    <el-table-column prop="product_price" label="Price" width="80"/>
    <el-table-column prop="product_type" label="Class" width="80" />
    <el-table-column prop="product_description" label="description"/>
    <el-table-column prop="is_recommended" label="Recommended" width="140"/>
    <el-table-column prop="on_shelf_date" label="upload time" width="180"/>
    <el-table-column prop="point_ratio" label="Pt ratio" width="80"/>
    <el-table-column >
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index)"
          >Edit</el-button
        ><el-button size="small" type="danger" @click="handleDelete(scope.$index)"
          >Delete</el-button
        >
      </template>
    </el-table-column>
  </el-table>

</template>

<script setup lang="ts">
import axios from 'axios';


const handleEdit = (index: number) => {
  location.href = '/#/a/manage/' + props.list![Math.floor(index)].product_id + ''
}
const handleDelete = (index: number) => {
  axios.post('/api/delete/product', {product_id: index})
    .then(_ => {
      alert('successfully deleted')
    })
    .catch(err => {
      console.error(err.message);
      
    })
  window.location.reload();
}

const props = defineProps({
  list: Array<any>
})

</script>

<style scoped>

</style>