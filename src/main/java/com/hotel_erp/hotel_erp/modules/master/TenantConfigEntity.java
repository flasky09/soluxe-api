package com.hotel_erp.hotel_erp.modules.master;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tenant_config")
@Data
public class TenantConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String subdomain;

    @Column(name = "hotel_name", nullable = false, length = 100)
    private String hotelName;

    @Column(name = "primary_color", length = 7)
    private String primaryColor;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "db_key", nullable = false, length = 50)
    private String dbKey;
}
