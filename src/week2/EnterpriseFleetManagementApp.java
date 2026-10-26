package week2;

import java.util.ArrayList;
import java.util.List;

/**
 * Week 2 - Capstone Demonstration: Enterprise Autonomous & Electric Logistics Fleet System
 * Author: YuvaIntern Java Development Intern
 * 
 * Integrated Architecture Breakdown:
 * 1. Encapsulation: Abstract class Vehicle encapsulates private fields (vehicleId, model,
 *    currentBatteryLevel, vin) with strict invariant validation guards and immutable VIN identifier.
 * 2. Abstraction: Contractual capability interfaces AutonomousNavigable and Rechargeable
 *    establish decoupled behavioral APIs across unrelated mechanical designs.
 * 3. Inheritance: Subclasses ElectricTruck, DeliveryVan, and AutonomousDrone inherit base telemetry,
 *    specializing battery capacity, payload dynamics, and maintenance calculations.
 * 4. Polymorphism: Heterogeneous fleet processing via base Vehicle references executing
 *    Dynamic Method Dispatch across overridden range, mission, and maintenance algorithms.
 * 5. Placement Advanced Patterns:
 *    - Java 16+ Pattern Matching for instanceof.
 *    - Deep vs. Shallow Cloning demonstration with nested Waypoint objects.
 *    - Final methods preventing tampering with mission auditing protocols.
 */
public class EnterpriseFleetManagementApp {

    // =========================================================================
    // Nested Helper Class for Deep vs. Shallow Cloning Demonstration
    // =========================================================================
    public static class Waypoint implements Cloneable {
        private String locationName;
        private double latitude;
        private double longitude;

        public Waypoint(String locationName, double latitude, double longitude) {
            this.locationName = locationName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getLocationName() { return locationName; }
        public void setLocationName(String locationName) { this.locationName = locationName; }

        @Override
        public Waypoint clone() {
            try {
                return (Waypoint) super.clone();
            } catch (CloneNotSupportedException e) {
                return new Waypoint(this.locationName, this.latitude, this.longitude);
            }
        }

        @Override
        public String toString() {
            return String.format("[%s (%.4f, %.4f)]", locationName, latitude, longitude);
        }
    }

    // =========================================================================
    // Interfaces (Contractual Abstraction)
    // =========================================================================
    public interface Rechargeable {
        void chargeBattery(double energyKwh);
        int getEstimatedChargingTimeMinutes(double targetPercentage);

        default void verifyChargerCompatibility(String plugStandard) {
            System.out.println("    [Rechargeable] Verified standard compatibility: " + plugStandard + " (CCS Type 2)");
        }
    }

    public interface AutonomousNavigable {
        void navigateTo(Waypoint destination);
        String getSensorTelemetry();

        default void performLiDARCalibration() {
            System.out.println("    [Autonomous Nav] LiDAR point-cloud recalibrated: 360-degree SLAM field active.");
        }
    }

    // =========================================================================
    // Abstract Superclass: Vehicle (Encapsulation + Template Skeleton)
    // =========================================================================
    public static abstract class Vehicle implements Cloneable {
        private final String vehicleId;
        private final String vin; // Vehicle Identification Number (Immutable)
        private String model;
        protected double currentBatteryLevel; // 0.0% to 100.0%
        protected double batteryCapacityKwh;
        protected Waypoint nextWaypoint;

        protected Vehicle(String vehicleId, String vin, String model, double initialBattery, double batteryCapacityKwh, Waypoint waypoint) {
            if (vehicleId == null || vehicleId.trim().isEmpty()) {
                throw new IllegalArgumentException("Vehicle ID cannot be empty.");
            }
            if (vin == null || vin.length() != 17) {
                throw new IllegalArgumentException("VIN must be a valid 17-character standard identifier.");
            }
            if (initialBattery < 0.0 || initialBattery > 100.0) {
                throw new IllegalArgumentException("Battery percentage must be between 0.0% and 100.0%.");
            }

            this.vehicleId = vehicleId;
            this.vin = vin;
            this.model = model;
            this.currentBatteryLevel = initialBattery;
            this.batteryCapacityKwh = batteryCapacityKwh;
            this.nextWaypoint = (waypoint != null) ? waypoint.clone() : new Waypoint("Depot Hub", 12.9716, 77.5946);
        }

        // Getters (Encapsulation)
        public String getVehicleId() { return vehicleId; }
        public String getVin() { return vin; }
        public String getModel() { return model; }
        public double getCurrentBatteryLevel() { return currentBatteryLevel; }
        public double getBatteryCapacityKwh() { return batteryCapacityKwh; }
        public Waypoint getNextWaypoint() { return nextWaypoint; }

        public void setNextWaypoint(Waypoint waypoint) {
            this.nextWaypoint = waypoint;
        }

        // Protected mutator with invariant checking
        protected void consumeBattery(double percent) {
            this.currentBatteryLevel = Math.max(0.0, this.currentBatteryLevel - percent);
        }

        // Abstract Methods (Subclasses must specialize)
        public abstract double calculateOperationalRangeKm();
        public abstract boolean dispatchMission(String destination, double payloadKg);
        public abstract double computeEstimatedMaintenanceCost();

        // Final Method: Invariant security policy cannot be overridden by subclasses
        public final void generateAuditLog(String action) {
            System.out.printf("    [SEC-AUDIT] VID=%-10s | VIN=%s | ACTION=%-18s | BATTERY=%.1f%%%n",
                    this.vehicleId, this.vin, action, this.currentBatteryLevel);
        }

        // Deep Clone implementation
        @Override
        public Vehicle clone() throws CloneNotSupportedException {
            Vehicle cloned = (Vehicle) super.clone();
            // Deep copy mutable nested Waypoint object!
            if (this.nextWaypoint != null) {
                cloned.nextWaypoint = this.nextWaypoint.clone();
            }
            return cloned;
        }
    }

    // =========================================================================
    // Subclass 1: ElectricTruck (Heavy Commercial Transport)
    // =========================================================================
    public static class ElectricTruck extends Vehicle implements Rechargeable {
        private double maxCargoTons;

        public ElectricTruck(String vehicleId, String vin, String model, double initialBattery, double maxCargoTons, Waypoint waypoint) {
            super(vehicleId, vin, model, initialBattery, 350.0, waypoint); // 350 kWh pack
            this.maxCargoTons = maxCargoTons;
        }

        @Override
        public double calculateOperationalRangeKm() {
            // Heavy truck consumes ~1.4 kWh per km
            double availableKwh = (this.currentBatteryLevel / 100.0) * this.batteryCapacityKwh;
            return availableKwh / 1.4;
        }

        @Override
        public boolean dispatchMission(String destination, double payloadKg) {
            double payloadTons = payloadKg / 1000.0;
            if (payloadTons > this.maxCargoTons) {
                System.out.printf("  [Truck %s] REJECTED: Payload %.2f tons exceeds capacity %.2f tons.%n",
                        this.getVehicleId(), payloadTons, this.maxCargoTons);
                return false;
            }
            double requiredRange = 180.0; // Simulated mission distance
            if (calculateOperationalRangeKm() < requiredRange) {
                System.out.printf("  [Truck %s] REJECTED: Range %.1f km is insufficient for mission (needs %.1f km).%n",
                        this.getVehicleId(), calculateOperationalRangeKm(), requiredRange);
                return false;
            }

            consumeBattery(45.0); // Mission battery consumption
            System.out.printf("  [Truck %s] MISSION DISPATCHED to %s with %.1f tons cargo. Battery now: %.1f%%%n",
                    this.getVehicleId(), destination, payloadTons, this.currentBatteryLevel);
            generateAuditLog("TRUCK_DISPATCH");
            return true;
        }

        @Override
        public double computeEstimatedMaintenanceCost() {
            return 8500.00; // Heavy axle and battery thermal maintenance
        }

        @Override
        public void chargeBattery(double energyKwh) {
            double percentAdded = (energyKwh / this.batteryCapacityKwh) * 100.0;
            this.currentBatteryLevel = Math.min(100.0, this.currentBatteryLevel + percentAdded);
            System.out.printf("  [Truck %s] Charged %.1f kWh. Battery level restored to %.1f%%%n",
                    this.getVehicleId(), energyKwh, this.currentBatteryLevel);
        }

        @Override
        public int getEstimatedChargingTimeMinutes(double targetPercentage) {
            double diff = Math.max(0.0, targetPercentage - this.currentBatteryLevel);
            return (int) (diff * 1.5); // DC Fast Charging curve
        }
    }

    // =========================================================================
    // Subclass 2: DeliveryVan (Urban Last-Mile Autonomous Logistics)
    // =========================================================================
    public static class DeliveryVan extends Vehicle implements Rechargeable, AutonomousNavigable {
        private int urbanPackageCapacity;

        public DeliveryVan(String vehicleId, String vin, String model, double initialBattery, int urbanPackageCapacity, Waypoint waypoint) {
            super(vehicleId, vin, model, initialBattery, 85.0, waypoint); // 85 kWh pack
            this.urbanPackageCapacity = urbanPackageCapacity;
        }

        @Override
        public double calculateOperationalRangeKm() {
            // Van consumes ~0.28 kWh per km
            double availableKwh = (this.currentBatteryLevel / 100.0) * this.batteryCapacityKwh;
            return availableKwh / 0.28;
        }

        @Override
        public boolean dispatchMission(String destination, double payloadKg) {
            if (this.currentBatteryLevel < 20.0) {
                System.out.printf("  [Van %s] REJECTED: Low battery warning (%.1f%%). Recharging mandatory.%n",
                        this.getVehicleId(), this.currentBatteryLevel);
                return false;
            }
            consumeBattery(25.0);
            System.out.printf("  [Van %s] AUTONOMOUS ROUTE engaged to %s. Cargo parcels loaded: %d. Battery: %.1f%%%n",
                    this.getVehicleId(), destination, this.urbanPackageCapacity, this.currentBatteryLevel);
            generateAuditLog("VAN_AUTONOMOUS_RUN");
            return true;
        }

        @Override
        public double computeEstimatedMaintenanceCost() {
            return 3200.00; // Tire wear and regenerative brake servicing
        }

        @Override
        public void chargeBattery(double energyKwh) {
            double percentAdded = (energyKwh / this.batteryCapacityKwh) * 100.0;
            this.currentBatteryLevel = Math.min(100.0, this.currentBatteryLevel + percentAdded);
            System.out.printf("  [Van %s] AC Fast Charging: Restored to %.1f%%%n", this.getVehicleId(), this.currentBatteryLevel);
        }

        @Override
        public int getEstimatedChargingTimeMinutes(double targetPercentage) {
            double diff = Math.max(0.0, targetPercentage - this.currentBatteryLevel);
            return (int) (diff * 0.8);
        }

        @Override
        public void navigateTo(Waypoint destination) {
            setNextWaypoint(destination);
            System.out.printf("  [Van %s Navigation] GPS Route locked: %s via Urban Highway-44%n",
                    this.getVehicleId(), destination);
        }

        @Override
        public String getSensorTelemetry() {
            return "Solid-State LiDAR: ACTIVE | 8x HDR Optical Cameras: 60FPS | Radar: 120m Sweep";
        }
    }

    // =========================================================================
    // Subclass 3: AutonomousDrone (Express Aerial Payload Courier)
    // =========================================================================
    public static class AutonomousDrone extends Vehicle implements Rechargeable, AutonomousNavigable {
        private double maxFlightCeilingMeters;

        public AutonomousDrone(String vehicleId, String vin, String model, double initialBattery, double maxFlightCeilingMeters, Waypoint waypoint) {
            super(vehicleId, vin, model, initialBattery, 12.0, waypoint); // 12 kWh aerial pack
            this.maxFlightCeilingMeters = maxFlightCeilingMeters;
        }

        @Override
        public double calculateOperationalRangeKm() {
            // Drone consumes ~0.15 kWh per km flight
            double availableKwh = (this.currentBatteryLevel / 100.0) * this.batteryCapacityKwh;
            return availableKwh / 0.15;
        }

        @Override
        public boolean dispatchMission(String destination, double payloadKg) {
            if (payloadKg > 15.0) { // Max 15kg for aerial express
                System.out.printf("  [Drone %s] REJECTED: Payload %.1f kg exceeds FAA drone limit (15.0 kg).%n",
                        this.getVehicleId(), payloadKg);
                return false;
            }
            consumeBattery(35.0);
            System.out.printf("  [Drone %s] TAKEOFF APPROVED to %s at %.0fm altitude. Battery: %.1f%%%n",
                    this.getVehicleId(), destination, this.maxFlightCeilingMeters, this.currentBatteryLevel);
            generateAuditLog("DRONE_FLIGHT_RUN");
            return true;
        }

        @Override
        public double computeEstimatedMaintenanceCost() {
            return 1450.00; // Rotor balancing and ultrasonic sensor recalibration
        }

        @Override
        public void chargeBattery(double energyKwh) {
            double percentAdded = (energyKwh / this.batteryCapacityKwh) * 100.0;
            this.currentBatteryLevel = Math.min(100.0, this.currentBatteryLevel + percentAdded);
            System.out.printf("  [Drone %s] Wireless Induction Pad: Battery at %.1f%%%n", this.getVehicleId(), this.currentBatteryLevel);
        }

        @Override
        public int getEstimatedChargingTimeMinutes(double targetPercentage) {
            double diff = Math.max(0.0, targetPercentage - this.currentBatteryLevel);
            return (int) (diff * 0.4); // Ultra-fast quick-swap / charge
        }

        @Override
        public void navigateTo(Waypoint destination) {
            setNextWaypoint(destination);
            System.out.printf("  [Drone %s Navigation] Direct 3D Airway vector locked to %s%n",
                    this.getVehicleId(), destination);
        }

        @Override
        public String getSensorTelemetry() {
            return "Dual Optical Flow + Ultrasonic Altitude Sonar + RTK-GPS (Centimeter Precision)";
        }
    }

    // =========================================================================
    // MAIN DEMONSTRATION WORKFLOW
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("====================================================================================");
        System.out.println("       YUVAINTERN LOGISTICS SYSTEMS - AUTONOMOUS & ELECTRIC FLEET MANAGER");
        System.out.println("       WEEK 2 CAPSTONE DEMONSTRATION: FULL OBJECT-ORIENTED ARCHITECTURE");
        System.out.println("====================================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Instantiating Heterogeneous Fleet (Inheritance + Encapsulation)
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Fleet Provisioning & State Initialization ---");

        Waypoint hubStation = new Waypoint("Central Bengaluru Logistics Hub", 12.9716, 77.5946);

        List<Vehicle> fleet = new ArrayList<>();
        fleet.add(new ElectricTruck("TRK-101", "1HTMK48D930291847", "Tata Prima EV Heavy", 92.0, 22.0, hubStation));
        fleet.add(new DeliveryVan("VAN-204", "1GBHG31J482910384", "Rivian EDV 700", 78.0, 150, hubStation));
        fleet.add(new AutonomousDrone("DRN-309", "1A8DU91X739201948", "DJI FlyCart Express", 88.0, 120.0, hubStation));
        fleet.add(new ElectricTruck("TRK-102", "1HTMK48D930291999", "Volvo FH Electric Semi", 22.0, 26.0, hubStation)); // Low battery for rejection test

        System.out.printf("Successfully provisioned %d multimodal autonomous & electric fleet assets.%n%n", fleet.size());

        // ---------------------------------------------------------------------
        // 2. Tabular Telemetry & Range Analysis (Runtime Dynamic Dispatch)
        // ---------------------------------------------------------------------
        System.out.println("--- 2. Fleet Telemetry & Dynamic Operational Range Matrix ---");
        System.out.println("+----------+------------------------+------------+---------------+----------------+----------------+");
        System.out.println("| Asset ID | Vehicle Model          | Class Type | Current Batt. | Est. Range     | Annual Maint.  |");
        System.out.println("+----------+------------------------+------------+---------------+----------------+----------------+");

        double totalFleetMaintenance = 0.0;
        for (Vehicle v : fleet) {
            // Polymorphic method invocations via Dynamic Method Dispatch
            double estRange = v.calculateOperationalRangeKm();
            double maintCost = v.computeEstimatedMaintenanceCost();
            totalFleetMaintenance += maintCost;

            String classType = (v instanceof ElectricTruck) ? "Semi-Truck" :
                               (v instanceof DeliveryVan)   ? "LastMile Van": "Express Drone";

            System.out.printf("| %-8s | %-22s | %-10s | %11.1f%% | %11.1f km | Rs. %10.2f |%n",
                    v.getVehicleId(), v.getModel(), classType, v.getCurrentBatteryLevel(), estRange, maintCost);
        }
        System.out.println("+----------+------------------------+------------+---------------+----------------+----------------+");
        System.out.printf("Total Projected Fleet Maintenance Budget: Rs. %,.2f%n%n", totalFleetMaintenance);

        // ---------------------------------------------------------------------
        // 3. Autonomous Navigation & Charging Contracts (Interface Abstraction)
        // ---------------------------------------------------------------------
        System.out.println("--- 3. Autonomous Navigation Contracts & Sensor Telemetry ---");
        Waypoint techParkDestination = new Waypoint("Electronic City Tech Park", 12.8399, 77.6770);

        for (Vehicle v : fleet) {
            // Java 16+ Pattern Matching for instanceof
            if (v instanceof AutonomousNavigable navigableUnit) {
                System.out.println("Asset [" + v.getVehicleId() + "] conforms to AutonomousNavigable Contract:");
                navigableUnit.performLiDARCalibration();
                navigableUnit.navigateTo(techParkDestination);
                System.out.println("    Sensor Rig -> " + navigableUnit.getSensorTelemetry());
                System.out.println();
            }
        }

        // ---------------------------------------------------------------------
        // 4. Mission Dispatch Engine (Polymorphism with Boundary Validation)
        // ---------------------------------------------------------------------
        System.out.println("--- 4. Mission Dispatch Simulation & Security Audit ---");
        // Dispatch test 1: Valid truck shipment
        fleet.get(0).dispatchMission("Whitefield Distribution Hub", 18500.0);

        // Dispatch test 2: Low-battery truck shipment (Expect refusal)
        fleet.get(3).dispatchMission("Mysuru Freight Depot", 12000.0);

        // Dispatch test 3: Drone shipment with excessive payload (Expect refusal)
        fleet.get(2).dispatchMission("Indiranagar Express Locker", 35.0);

        // Dispatch test 4: Van parcel run (Success)
        fleet.get(1).dispatchMission("Koramangala Commercial Hub", 450.0);

        // ---------------------------------------------------------------------
        // 5. Rechargeable Capability Invocations
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. Energy Grid Replenishment (Rechargeable Interface) ---");
        for (Vehicle v : fleet) {
            if (v instanceof Rechargeable rechargeableUnit) {
                if (v.getCurrentBatteryLevel() < 60.0) {
                    System.out.printf("Asset [%s] low battery (%.1f%%). Requesting Grid DC Fast Charging:%n",
                            v.getVehicleId(), v.getCurrentBatteryLevel());
                    rechargeableUnit.verifyChargerCompatibility("CCS-2 Combo");
                    int timeMins = rechargeableUnit.getEstimatedChargingTimeMinutes(90.0);
                    System.out.printf("    Estimated Time to 90%%: %d minutes.%n", timeMins);
                    rechargeableUnit.chargeBattery(120.0);
                    System.out.println();
                }
            }
        }

        // ---------------------------------------------------------------------
        // 6. Placement Edge Case: Deep vs. Shallow Cloning Demonstration
        // ---------------------------------------------------------------------
        System.out.println("--- 6. Placement Edge Case: Deep vs. Shallow Object Cloning ---");
        try {
            DeliveryVan originalVan = (DeliveryVan) fleet.get(1);
            DeliveryVan clonedVan = (DeliveryVan) originalVan.clone();

            System.out.println("Original Van Next Waypoint: " + originalVan.getNextWaypoint());
            System.out.println("Cloned Van Next Waypoint  : " + clonedVan.getNextWaypoint());

            System.out.println("\nMutating cloned van's waypoint location name to 'Airport Cargo Terminal'...");
            clonedVan.getNextWaypoint().setLocationName("Airport Cargo Terminal");

            System.out.println("Result Check:");
            System.out.println("  Original Van Waypoint: " + originalVan.getNextWaypoint().getLocationName() + " (UNTOUCHED! Deep copy verified)");
            System.out.println("  Cloned Van Waypoint  : " + clonedVan.getNextWaypoint().getLocationName());
            System.out.println("  Waypoint Memory Reference Match? (original == clone): " +
                    (originalVan.getNextWaypoint() == clonedVan.getNextWaypoint()) + " (Different Heap addresses)");
        } catch (CloneNotSupportedException e) {
            System.err.println("Cloning failure: " + e.getMessage());
        }

        System.out.println("\n====================================================================================");
        System.out.println("  CAPSTONE FLEET SIMULATION CONCLUDED: ZERO CONTRACT OR LOGIC VIOLATIONS");
        System.out.println("====================================================================================\n");
    }
}
