<script setup>
import { onMounted, ref } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const map = ref(null);
const searchQuery = ref("");

onMounted(() => {
 

  map.value = L.map('mapContainer').setView([10.7626, 106.6602], 13); // HCMC coords

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map.value);

     const kfcStores = [
  { name: "KFC Bến Cát", lat: 11.1255781, lng: 106.6034281 },
  { name: "KFC Lê Văn Sỹ", lat: 10.7887663, lng: 106.6759051 },
  { name: "KFC Ngô Gia Tự", lat: 10.7672778, lng: 106.674303 }
];

kfcStores.forEach(store => {
  L.marker([store.lat, store.lng])
   .addTo(map.value)
   .bindPopup(`<b>${store.name}</b><br>Đang mở cửa`);
});
});

const handleSearch = async () => {
  if (!searchQuery.value) return;

  const response = await fetch(
    `https://nominatim.openstreetmap.org/search?format=json&q=${searchQuery.value}&countrycodes=vn`
  );
  const data = await response.json();

  if (data.length > 0) {
    const { lat, lon } = data[0];
    map.value.setView([lat, lon], 16);
    L.marker([lat, lon]).addTo(map.value);
  } else {
    alert("Không tìm thấy địa chỉ này!");
  }
};
</script>

<template>
  <div class="relative w-full h-[700px] border border-gray-200">
    <div class="absolute top-4 left-4 z-[1000] flex bg-white shadow-md p-1 rounded">
      <input 
        v-model="searchQuery"
        type="text" 
        placeholder="Nhập địa chỉ của bạn..." 
        class="p-2 w-64 outline-none text-sm"
        @keyup.enter="handleSearch"
      />
      <button @click="handleSearch" class="bg-red-600 text-white px-4 py-2 text-sm font-bold uppercase">
        Tìm
      </button>
    </div>

    <div id="mapContainer" class="w-full h-full"></div>
  </div>
</template>

<style scoped>

#mapContainer {
  z-index: 1;
}
</style>