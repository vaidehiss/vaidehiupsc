package in.nic.upscora.graphql.form4.utils;


import lombok.Getter;

@Getter
public class IdComponents {
    private final long id;
        private final long timestamp;
        private final long machineId;
        private final long sequence;
        private final String timestampIso;
        
        public IdComponents(long id, long timestamp, long machineId, long sequence) {
            this.id = id;
            this.timestamp = timestamp;
            this.machineId = machineId;
            this.sequence = sequence;
            this.timestampIso = new java.util.Date(timestamp).toInstant().toString();
        }
        
        @Override
        public String toString() {
            return String.format("ID: %d, Timestamp: %s, Machine: %d, Sequence: %d",
                id, timestampIso, machineId, sequence);
        }
    
}
