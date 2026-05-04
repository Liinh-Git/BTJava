import org.jobportal.config.DatabaseConfig;

public static void main(String[] args) {
    if (DatabaseConfig.checkConnection()) {
        System.out.println("Hệ thống sẵn sàng!");
        // Gọi giao diện View ở đây
    } else {
        System.out.println("Lỗi hệ thống, vui lòng kiểm tra lại Database.");
    }
}