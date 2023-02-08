package com.portsip.sipsample.modification.src.models;

public class Constants {

    public static String WRONG_RESPONSE_MESSAGE_ADMIN = "wrong_authentication";
    public static String CORRECT_RESPONSE_MESSAGE_ADMIN = "login successfully";
    public static String BASE_URL = "http://202.59.208.112";
    public static String WRONG_DATA_PICK = "Please put correct information";

    /*@ Concatenate the Admin Password at the end*/
    public static String URL_ADMIN_LOGIN = BASE_URL + "/vending/admin/login/app_login?username=tab_admin&unique_pin=";

    public static String URL_RIDER_LOGIN = BASE_URL + "/vending/admin/login/app_login?user_group=locker_rider_group&username=locker_rider&unique_pin=";


    public static String URL_PARCEL_INFO = BASE_URL + "/vending/admin/login/rider_response?customer_phone=%s&order_id=%s";


    public static String URL_PICK_UP = BASE_URL + "/vending/admin/login/customer_pickup?customer_phone=%s&customer_otp=%s";


    /**
     * ({@link String}  machine_id,{@link String}  rider_pin,{@link String}  box_number ,{@link String}  board_no)
     */
    public static String URL_RIDER_OPEN = BASE_URL + "/vending/api/parcel_box.php?machine_id=%s&user_type=rider&rider_pin=%s&box_number=%s&board_no=%s&box_status=opened";

    /**
     * ({@link String}  machine_id,{@link String}  rider_pin,{@link String}  box_number ,{@link String}  board_no)
     */
    public static String URL_RIDER_CLOSE = BASE_URL + "/vending/api/parcel_box.php?machine_id=%s&user_type=rider&rider_pin=%s&box_number=%s&board_no=%s&box_status=closed";


    /**
     * ({@link String} machine_id,{@link String} user_phone,{@link String} board_no ,{@link String} box_no)
     */
    public static String URL_CUSTOMER_OPEN = BASE_URL + "/vending/api/parcel_box.php?machine_id=%s&user_type=customer&customer_phone=%s&board_no=%s&box_number=%s&box_status=opened";

    /**
     * ({@link String} machine_id,{@link String} user_phone,{@link String} board_no ,{@link String} box_no)
     */
    public static String URL_CUSTOMER_CLOSE = BASE_URL + "/vending/api/parcel_box.php?machine_id=%s&user_type=customer&customer_phone=%s&board_no=%s&box_number=%s&box_status=closed";


    public static String URL_CUSTOMER_LOGIN = BASE_URL + "/vending/admin/login/app_login?user_group=locker_customer&username=locker_customer&unique_pin=123456";
}
