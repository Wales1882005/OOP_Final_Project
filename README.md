# SDG 13 Carbon Footprint Tracking & Offset Management System

## Overview
This Java desktop application supports carbon footprint tracking, climate project auditing, and offset management aligned with SDG 13 (Climate Action).

The app lets users add, update, delete, and save carbon activity logs with validation rules for offset entries. It also maintains an audit trail for all actions and calculates net emissions.

## Key Features
- Add carbon activity logs with activity description, CO₂ amount, and category
- Track both emissions and verified offset entries
- Validate offset activity descriptions for certificate/reference IDs
- Audit log generation for create/update/delete/save/load operations
- Save and load data from `climate_logs.txt`
- Reject invalid values and require proper offset verification

## Project Structure
- `src/com/cityu/sdg/gui/MainDashboardGUI.java` - main Swing user interface
- `src/com/cityu/sdg/service/DataManager.java` - persistence, auditing, ID generation, and emission calculations
- `src/com/cityu/sdg/model/CarbonLog.java` - carbon activity log model with validation and serialization
- `src/com/cityu/sdg/model/User.java` - abstract stakeholder base class
- `src/com/cityu/sdg/model/CorporateUser.java` - corporate user specialization
- `src/com/cityu/sdg/model/ClimateProjectUser.java` - climate project user specialization and prioritization
- `src/com/cityu/sdg/service/Prioritizable.java` - interface for priority scoring

## Running the Application
1. Ensure Java 24 or later is installed.
2. Compile the project in your IDE or using the command line.
3. Run the main class:
   ```bash
   java -cp out com.cityu.sdg.gui.MainDashboardGUI
   ```

> If your build output directory differs, update the classpath accordingly.

### Example Command Line Build
```bash
javac -d out src/com/cityu/sdg/gui/MainDashboardGUI.java src/com/cityu/sdg/model/*.java src/com/cityu/sdg/service/*.java
java -cp out com.cityu.sdg.gui.MainDashboardGUI
```

## Screenshot
The application opens as a desktop Swing window with:
- a header for SDG 13 climate action
- input fields for activity description, CO₂ amount, and category
- controls to add, update, delete, and save logs
- a table showing log ID, description, emissions, category, and high impact flag
- a net carbon footprint summary at the bottom

> Add a screenshot image file and update this section with the actual screenshot path once available.

## Troubleshooting
- `Error: Could not find or load main class`: confirm `out` contains compiled classes and use the correct package path.
- `NoClassDefFoundError`: ensure all source files are compiled before running.
- `IOException` while saving logs: verify write permissions in the project folder.
- Offset validation fails: include `cert`, `ref:`, `id:`, or `credit` in the activity description for "Offset" category entries.

## Persistence Files
- `climate_logs.txt` - stores saved carbon log records
- `audit_trail.log` - records audit trail events for create, update, delete, save, and load operations

## Notes
- Offset entries must include a certificate or reference ID to pass validation.
- The net carbon footprint display treats "Offset" as negative emissions.

## License
This project is distributed under the terms of the included `LICENSE` file.
