    <template>
      <div>
        <ul
          class="tweets-list"
          v-infinite-scroll="onLoad"
          infinite-scroll-delay="1000"
          infinite-scroll-distance="300"
          :v-if="tweets.length > 0"
        >
        
          <li class="tweet-wrapper" v-for="item in getTweetVal()" :key="item.user_id">
            <TweetCard
              :user_id="item.user_id"
              :username="item.username"
              :text="item.text"
              :avatarUrl="item.avatarUrl"
              :starCount="item.starCount"
              :replyCount="item.replyCount"
              :retweetCount="item.retweetCount"
            />
          </li>
          
        </ul>
      </div>
    </template>

    <script setup lang="ts">
    import TweetCard from "./TweetCard.vue";
    import { defineProps, ref } from "vue";
    import axios from "axios"

    interface Tweet {
      user_id: string;
      username: string;
      avatarUrl: string | undefined;
      text: string;
      imagesUrl: string[] | undefined;
      replyCount: number;
      starCount: number;
      retweetCount: number;
    }



    const tweets = ref<Tweet[]>([]);
    const getTweetVal = () =>{
      return tweets.value;
    }
    let currPage :number = 1;
    let hasNextPage :boolean = true;
    const onLoad = async () => {
      // 发起异步请求获取新的 tweets 数据
      // 当有下一页时一直请求
      while (hasNextPage) {
        const newTweets = await fetchNewTweets();
      // 将新数据合并到现有的 tweets 数组中
        tweets.value.push(...newTweets);
        console.log(tweets);
        
        currPage++
      }
    };
    

    const fetchNewTweets = async (): Promise<Tweet[]> => {
      // 发起异步请求获取新的 tweets 数据
      return new Promise((resolve, reject) => {
        const timelineAPI = '/api/a/get-time-line/' // full URI: /api/a/get-time-line/:page
        
        axios.get(timelineAPI + currPage)
          .then((response) => {
            const data = response.data
            const tweetsData = data.tweets
            hasNextPage = data.hasNextPage
            resolve(tweetsData)
          })
          .catch((error) => {
            console.log(error);
          })
      });
    };
    
    </script>

    <style scoped>

  .tweet-wrapper {
      min-width: 300px;
      width: 50vw;
      padding: 5px;
      border-radius: 5px;
      text-align: left;
      word-wrap: break-word;
    }

    .tweets-list {
      list-style: none;
      margin: 0;
      padding: 0;
    }

    </style>