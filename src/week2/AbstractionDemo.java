package week2;

/**
 * Week 2 - Topic 5: Abstraction, Abstract Classes, Interfaces, and the Template Method Pattern
 * Author: YuvaIntern Java Development Intern
 * 
 * Principal Architectural Takeaways:
 * - Abstraction hides internal implementation complexities while exposing an intuitive, standardized interface.
 * 
 * 1. Abstract Class:
 *    - Cannot be directly instantiated via 'new'.
 *    - May contain both abstract methods (no body) and concrete methods (with body).
 *    - Maintains instance state (instance fields) and possesses constructors (invoked by subclasses via super()).
 *    - Ideal for the Template Method Pattern: Defines an invariant execution skeleton while delegating
 *      variant steps to concrete subclasses.
 * 
 * 2. Interface:
 *    - Represents a strict behavioral capability contract ("CAN-DO" relationship).
 *    - Supports multiple inheritance of type (a class can implement multiple interfaces).
 *    - Fields are implicitly 'public static final' constants.
 *    - Methods (prior to Java 8) were strictly 'public abstract'.
 *    - Java 8+ added 'default' methods (providing default implementation without breaking existing implementations)
 *      and 'static' utility methods.
 *    - Java 9+ added 'private' helper methods to prevent code duplication among default methods.
 * 
 * 3. Enterprise Decision Matrix:
 *    - Use Abstract Class when: Closely related classes share common state/code and constructor lifecycle.
 *    - Use Interface when: Unrelated classes must adhere to a common capability contract or API boundary.
 */
public class AbstractionDemo {

    // =========================================================================
    // 1. Interfaces: Capability Contracts
    // =========================================================================
    public interface EncryptedService {
        // Constant (implicitly public static final)
        String CIPHER_ALGORITHM = "AES-256-GCM";

        // Abstract method
        String encryptPayload(String rawData);

        // Java 8+ Default Method: Provides fallback behavior
        default void verifyEncryptionCompliance() {
            System.out.println("  [Interface Default] Compliance Check Passed: Protocol conforms to " + CIPHER_ALGORITHM);
            logInternalDiagnostics(); // Calling Java 9 private interface method
        }

        // Java 8+ Static Utility Method
        static boolean isKeyLengthSufficient(int keyBits) {
            return keyBits >= 256;
        }

        // Java 9+ Private Interface Helper Method (Encapsulates internal logic)
        private void logInternalDiagnostics() {
            System.out.println("  [Interface Private Helper] Zero-knowledge key rotation audit logged.");
        }
    }

    public interface AuditableService {
        void recordAuditLog(String action, String operatorId);
    }

    // =========================================================================
    // 2. Abstract Class: CloudStorageProvider (Template Method Pattern)
    // =========================================================================
    public static abstract class CloudStorageProvider implements EncryptedService, AuditableService {
        // State retention: Instance variables in abstract class
        protected String providerName;
        protected String storageBucket;
        protected long totalBytesStored;

        // Abstract class constructor (Called during subclass constructor chaining)
        public CloudStorageProvider(String providerName, String storageBucket) {
            this.providerName = providerName;
            this.storageBucket = storageBucket;
            this.totalBytesStored = 0;
        }

        // --- Template Method (Pattern): Marked 'final' so subclasses cannot alter the algorithm skeleton ---
        public final void uploadObject(String objectKey, String rawData, String operatorId) {
            System.out.println("\n  === Starting Template Method: uploadObject() for " + objectKey + " ===");
            connectToBucket();                                  // Step 1: Subclass-specific connection
            String encryptedData = encryptPayload(rawData);     // Step 2: Interface encryption contract
            writeBytesToMedia(objectKey, encryptedData);        // Step 3: Subclass-specific write
            recordAuditLog("UPLOAD:" + objectKey, operatorId);  // Step 4: Interface audit contract
            disconnectFromBucket();                             // Step 5: Concrete base cleanup
            System.out.println("  === Template Method Completed Successfully ===");
        }

        // Abstract methods: Subclasses MUST provide these concrete steps
        protected abstract void connectToBucket();
        protected abstract void writeBytesToMedia(String objectKey, String encryptedPayload);

        // Concrete method shared by all storage providers
        protected void disconnectFromBucket() {
            System.out.printf("  [Base Cleanup] Session disconnected from bucket '%s' on %s.%n",
                    this.storageBucket, this.providerName);
        }

        // Implementing AuditableService contract at abstract base level
        @Override
        public void recordAuditLog(String action, String operatorId) {
            System.out.printf("  [Audit Trail] Action: %-20s | Operator: %-15s | Timestamp: %d%n",
                    action, operatorId, System.currentTimeMillis());
        }

        public long getTotalBytesStored() {
            return this.totalBytesStored;
        }
    }

    // =========================================================================
    // 3. Concrete Subclass 1: AwsS3StorageProvider
    // =========================================================================
    public static class AwsS3StorageProvider extends CloudStorageProvider {
        private String awsRegion;

        public AwsS3StorageProvider(String storageBucket, String awsRegion) {
            super("Amazon Web Services (AWS S3)", storageBucket);
            this.awsRegion = awsRegion;
        }

        @Override
        protected void connectToBucket() {
            System.out.printf("  [AWS Connect] Authenticating IAM SigV4 into S3 bucket '%s' [Region: %s]...%n",
                    this.storageBucket, this.awsRegion);
        }

        @Override
        public String encryptPayload(String rawData) {
            // Simulated AWS KMS Encryption
            return "AWS-KMS-CIPHER{" + Integer.toHexString(rawData.hashCode()) + "}";
        }

        @Override
        protected void writeBytesToMedia(String objectKey, String encryptedPayload) {
            long size = encryptedPayload.length() * 1024L; // Simulated byte size
            this.totalBytesStored += size;
            System.out.printf("  [AWS S3 PutObject] Uploaded '%s' (%d bytes) with SHA-256 integrity hash.%n",
                    objectKey, size);
        }
    }

    // =========================================================================
    // 4. Concrete Subclass 2: AzureBlobStorageProvider
    // =========================================================================
    public static class AzureBlobStorageProvider extends CloudStorageProvider {
        private String storageAccountName;

        public AzureBlobStorageProvider(String storageBucket, String storageAccountName) {
            super("Microsoft Azure Blob Storage", storageBucket);
            this.storageAccountName = storageAccountName;
        }

        @Override
        protected void connectToBucket() {
            System.out.printf("  [Azure Connect] Establishing SAS token session with Account '%s' -> Container '%s'...%n",
                    this.storageAccountName, this.storageBucket);
        }

        @Override
        public String encryptPayload(String rawData) {
            // Simulated Azure Key Vault Encryption
            return "AZURE-VAULT-CIPHER{" + Integer.toHexString(rawData.hashCode()) + "}";
        }

        @Override
        protected void writeBytesToMedia(String objectKey, String encryptedPayload) {
            long size = encryptedPayload.length() * 2048L;
            this.totalBytesStored += size;
            System.out.printf("  [Azure BlockBlob] Written block list for '%s' (%d bytes) across Geo-Redundant Tier.%n",
                    objectKey, size);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 2 - TOPIC 5: ABSTRACTION, TEMPLATE METHOD & INTERFACE CONTRACTS");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Interface Static and Default Method Demonstrations
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Interface Static Utility & Default Methods ---");
        boolean keyCheck = EncryptedService.isKeyLengthSufficient(256);
        System.out.println("Interface Static Method Check (256-bit Key Sufficient?): " + keyCheck);

        // ---------------------------------------------------------------------
        // 2. Concrete Polymorphic Storage Execution via Template Method
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Template Method Pattern Execution ---");

        CloudStorageProvider s3Provider = new AwsS3StorageProvider("production-analytics-bucket", "ap-south-1 (Mumbai)");
        s3Provider.verifyEncryptionCompliance(); // Invokes interface default method
        s3Provider.uploadObject("user_telemetry_2026.parquet", "RAW_BINARY_DATA_STREAM_88291", "eng-admin-01");

        CloudStorageProvider azureProvider = new AzureBlobStorageProvider("backup-archive-container", "corpstorageeast");
        azureProvider.verifyEncryptionCompliance();
        azureProvider.uploadObject("financial_audit_q3.csv", "FINANCIAL_LEDGER_DATA_CONFIDENTIAL", "finance-sec-09");

        // ---------------------------------------------------------------------
        // 3. Architectural Comparison: Abstract Class vs Interface Decision Matrix
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Enterprise Architectural Trade-Off Analysis ---");
        System.out.println("  +---------------------------+-----------------------------------+-----------------------------------+");
        System.out.println("  | Architectural Dimension   | Abstract Class                    | Interface                         |");
        System.out.println("  +---------------------------+-----------------------------------+-----------------------------------+");
        System.out.println("  | State Retention           | Retains instance state (fields)   | Stateless (constants only)        |");
        System.out.println("  | Inheritance Model         | Single inheritance (extends 1)    | Multiple inheritance (implements N|");
        System.out.println("  | Constructor Lifecycle     | Has constructors (via super())    | No constructors                   |");
        System.out.println("  | Design Pattern Alignment  | Template Method Pattern           | Strategy, Observer, Factory       |");
        System.out.println("  | Primary Relationship      | Strict IS-A hierarchy             | Flexible CAN-DO capability        |");
        System.out.println("  +---------------------------+-----------------------------------+-----------------------------------+");

        System.out.println("\n==========================================================================");
        System.out.println("  TOPIC 5 DEMONSTRATION COMPLETE: CONTRACTUAL ABSTRACTION VERIFIED");
        System.out.println("==========================================================================\n");
    }
}
