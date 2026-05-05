<script setup>
import { onMounted, ref } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { branchApi } from '@/api/branch';

const map = ref(null);
const searchQuery = ref("");
const markers = ref([]); 

const loadStores = async () => {
  try {
    const stores = await branchApi.getAllBranches();
    
    stores.forEach(store => {
      const marker = L.marker([store.latitude, store.longitude])
        .addTo(map.value)
        .bindPopup(`
          <div class="p-2">
            <b class="text-red-600">${store.name}</b><br>
            <span class="text-xs text-gray-600">${store.address}</span><br>
            <button class="mt-2 bg-red-600 text-white text-xs px-2 py-1 rounded">Đặt ngay</button>
          </div>
        `);
      markers.value.push(marker);
    });
  } catch (error) {
    console.error(error.message);
  }
};

onMounted(() => {
  
  map.value = L.map('mapContainer').setView([10.7626, 106.6602], 13);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map.value);


  loadStores();
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
    
    L.marker([lat, lon]).addTo(map.value).bindPopup("Vị trí của bạn").openPopup();
  } else {
    alert("Không tìm thấy địa chỉ này!");
  }
};
</script>

<template>
  <div class="relative w-full h-[700px] border border-gray-200">
    <!-- Search Overlay -->
    <div class="absolute top-4 left-4 z-[1000] flex bg-white shadow-md p-1 rounded">
      <input 
        v-model="searchQuery"
        type="text" 
        placeholder="Nhập địa chỉ của bạn..." 
        class="p-2 w-64 outline-none text-sm"
        @keyup.enter="handleSearch"
      />
      <button @click="handleSearch" class="bg-red-600 text-white px-4 py-2 text-sm font-bold uppercase hover:bg-red-700 transition">
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

:deep(.leaflet-popup-content-wrapper) {
  border-radius: 4px;
}
</style>