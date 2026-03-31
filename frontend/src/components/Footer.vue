<template>
  <footer class="bg-[#202124] text-white pt-12 pb-8 px-4 font-sans">
    <div class="max-w-7xl mx-auto">
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-8 mb-12">
        
        <div>
          <h3 class="font-bold text-lg mb-4">Danh Mục Món Ăn</h3>
          <ul class="space-y-2">
            <li v-for="item in footerNavigation.menu" :key="item.name">
              <a :href="item.href" class="text-gray-400 hover:text-white text-sm transition">{{ item.name }}</a>
            </li>
          </ul>
        </div>

        <div>
          <h3 class="font-bold text-lg mb-4">Về KFC</h3>
          <ul class="space-y-2">
            <li v-for="item in footerNavigation.about" :key="item.name">
              <a :href="item.href" class="text-gray-400 hover:text-white text-sm transition">{{ item.name }}</a>
            </li>
          </ul>
        </div>

        <div>
          <h3 class="font-bold text-lg mb-4">Liên hệ KFC</h3>
          <ul class="space-y-2">
            <li v-for="item in footerNavigation.contact" :key="item.name">
              <a :href="item.href" class="text-gray-400 hover:text-white text-sm transition">{{ item.name }}</a>
            </li>
          </ul>
        </div>

        <div>
          <h3 class="font-bold text-lg mb-4">Chính sách</h3>
          <ul class="space-y-2">
            <li v-for="item in footerNavigation.policy" :key="item.name">
              <a :href="item.href" class="text-gray-400 hover:text-white text-sm transition">{{ item.name }}</a>
            </li>
          </ul>
        </div>

        <div class="col-span-2 lg:col-span-1">
          <h3 class="font-bold text-lg mb-4">Download App</h3>
          <div class="flex flex-col gap-3">
            <a href="#"><img src="https://upload.wikimedia.org/wikipedia/commons/3/3c/Download_on_the_App_Store_Badge.svg" alt="App Store" class="w-32"></a>
            <a href="#"><img src="https://upload.wikimedia.org/wikipedia/commons/7/78/Google_Play_Store_badge_EN.svg" alt="Google Play" class="w-32"></a>
          </div>
        </div>
      </div>

      <div class="flex flex-col md:flex-row justify-between items-center border-b border-gray-700 pb-6 mb-8 text-xs text-gray-500">
        <p>Copyright © 2023 KFC Vietnam</p>
        <div class="flex gap-4 mt-4 md:mt-0">
          <a href="#" class="w-8 h-8 rounded-full bg-gray-700 flex items-center justify-center hover:bg-white hover:text-black transition italic font-bold">f</a>
          <a href="#" class="w-8 h-8 rounded-full bg-gray-700 flex items-center justify-center hover:bg-white hover:text-black transition">IG</a>
          <a href="#" class="w-8 h-8 rounded-full bg-gray-700 flex items-center justify-center hover:bg-white hover:text-black transition">YT</a>
          <a href="#" class="w-8 h-8 rounded-full bg-gray-700 flex items-center justify-center hover:bg-white hover:text-black transition">TW</a>
        </div>
      </div>

      <div class="flex flex-col md:flex-row justify-between items-start gap-6">
        <div class="text-sm text-gray-400 leading-relaxed uppercase">
          <h2 class="font-bold text-white text-lg mb-2">CÔNG TY LIÊN DOANH TNHH KFC VIỆT NAM</h2>
          <p>Số 292 Bà Triệu, P. Lê Đại Hành, Q. Hai Bà Trưng, TP. Hà Nội.</p>
          <p>Điện thoại: (028) 38489828</p>
          <p>Email: <span class="lowercase">lienhe@kfcvietnam.com.vn</span></p>
          <p>Mã số thuế: 0100773885</p>
          <p>Ngày cấp: 29/10/1998 - Nơi cấp: Cục Thuế Thành Phố Hà Nội</p>
        </div>
        
        <div>
          <img src="https://hanhchinhcong.vip/wp-content/uploads/2022/04/logo-da-thong-bao-bo-cong-thuong.png" alt="Đã thông báo Bộ Công Thương" class="h-12 object-contain">
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
const footerNavigation = {
  menu: [
    { name: 'Ưu Đãi', href: '#' },
    { name: 'Món Mới', href: '#' },
    { name: 'Combo 1 Người', href: '#' },
    { name: 'Combo Nhóm', href: '#' },
    { name: 'Gà Rán - Gà Quay', href: '#' },
    { name: 'Burger - Cơm - Mì Ý', href: '#' },
    { name: 'Thức Ăn Nhẹ', href: '#' },
    { name: 'Thức Uống & Tráng Miệng', href: '#' },
    { name: 'Bảng Thành Phần Dị Ứng', href: '#' },
  ],
  about: [
    { name: 'Câu Chuyện Của Chúng Tôi', href: '#' },
    { name: 'Tin Khuyến Mãi', href: '#' },
    { name: 'Tin tức KFC', href: '#' },
    { name: 'Tuyển dụng', href: '#' },
    { name: 'Đặt Tiệc Sinh Nhật', href: '#' },
    { name: 'Đơn Lớn Giá Hời', href: '#' },
  ],
  contact: [
    { name: 'Theo dõi đơn hàng', href: '#' },
    { name: 'Hệ Thống Nhà Hàng', href: '#' },
    { name: 'Liên hệ KFC', href: '#' },
  ],
  policy: [
    { name: 'Chính sách hoạt động', href: '#' },
    { name: 'Chính sách và quy định', href: '#' },
    { name: 'Chính sách bảo mật thông tin', href: '#' },
  ]
}
</script>