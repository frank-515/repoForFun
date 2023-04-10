<script setup>
import TweetCard from "./TweetCard.vue";
const props = defineProps({
  tweets: {
    type: Array,
    default: () => [
      {
        user_id: '',
        username: '',
        avatarUrl: '',
        text: '',
        imagesUrl: [],
      }
    ]
  }
});
</script>

<template>
  <div>
    <span v-for="(item, index) in tweets" :key="index">
		<span class="tweet-wrapper">
			<TweetCard :user_id="item.user_id" :username="item.username" :text="item.text" :avatarUrl="item.avatarUrl" />
		</span>
		
	</span>
  </div>
</template>

<style scoped>
.tweet-wrapper {
  width: auto;
  min-width: 300px;
  max-width: 400px;
  padding: 5px;
  border-radius: 5px;
  text-align: left;
  word-wrap: break-word;
}
</style>