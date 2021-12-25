<?php
$connect = mysqli_connect("localhost", "root","","quanlynv");
mysqli_query($connect, "SET NAMES 'utf8'");
$query = "SELECT * FROM `nhanvien`";
$data = mysqli_query($connect, $query);
class Employee{
    function Employee($maNV, $tenNV, $gioiTInh, $email, $tenDangNhap, $matKhau, $SDT, $maquyen){
        $this ->MaNV = $maNV;
        $this-> HoTen = $tenNV;
        $this->GioiTinh = $gioiTInh;
        $this ->Email = $email;
        $this -> TenDangNhap = $tenDangNhap;
        $this-> MatKhau = $matKhau;
        $this->SDT = $SDT;
        $this ->MaQuyen = $maquyen;
    }
}
// 2. Tạo mảng
$mangEp = array();
while($row = mysqli_fetch_assoc($data)){
    array_push($mangEp, new Employee($row[ 'maNV'], $row[ 'tenNV'], $row['gioiTinh'], $row['Email'], $row[ 'tenDangNhap'], $row[ 'matKhau'], $row['soDienThoai'], $row['maquyen']));
}
// 4. Chuyển định dạng của mảng -> JSON
echo json_encode($mangEp);
?>