CREATE SCHEMA `punchtimesystem` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;

CREATE TABLE `punchtimesystem`.`user_list` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `mac_address` VARCHAR(12) NOT NULL,
                                               `student_number` VARCHAR(15) NOT NULL,
                                               `last_name` VARCHAR(20) NOT NULL,
                                               `first_name` VARCHAR(20) NOT NULL,
                                               `last_modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                               PRIMARY KEY (`id`),
                                               UNIQUE INDEX `mac_address_UNIQUE` (`mac_address` ASC) VISIBLE,
                                               UNIQUE INDEX `student_number_UNIQUE` (`student_number` ASC) VISIBLE);
