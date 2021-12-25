<?php
$connect = mysqli_connect("localhost", "root","","quanlynv");
mysqli_query($connect, "SET NAMES 'utf8'");
$query = "SELECT c.id, c.maNV, n.tenNV, c.ngayRa, c.gioRa FROM `quanlyra`as c, `nhanvien` as n WHERE c.maNV = n.maNV";
$data = mysqli_query($connect, $query);
class Employee{
    function Employee($id, $maNV, $tenNV, $ngayRa, $gioRa){
        $this->Id = $id;
        $this-> MaNV = $maNV;
        $this -> TenNV = $tenNV;
        $this->NgayRa = $ngayRa;
        $this ->GioRa = $gioRa;
    }
}
// 2. Tạo mảng
$mangEp = array();
while($row = mysqli_fetch_assoc($data)){
    array_push($mangEp, new Employee($row['id'], $row[ 'maNV'],$row['tenNV'], $row['ngayRa'], $row['gioRa']));
}
// 4. Chuyển định dạng của mảng -> JSON
echo json_encode($mangEp);
?>