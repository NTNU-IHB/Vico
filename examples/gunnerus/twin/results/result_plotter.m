clc, clear, close all;

saveimg = true;
dirPath = "data2";
plotDir = strcat(dirPath, '/plots');
if saveimg
   if isfolder(plotDir) == false
      mkdir(plotDir) 
   end
end

gunnerus = read_data(dirPath + "/gunnerus.csv");
vessel = read_data(dirPath + "/vesselModel.csv");
vesselModelObserver = read_data(dirPath + "/vesselModelObserver.csv");
portAzimuth = read_data(dirPath + "/portAzimuth.csv");
starboardAzimuth = read_data(dirPath + "/starboardAzimuth.csv");

t = gunnerus(:,1);
t = t - t(1); % t starts at 0

gunnerus_north = gunnerus(:,3);
gunnerus_east = gunnerus(:,4);
gunnerus_heading = gunnerus(:,6);
gunnerus_true_heading = gunnerus(:,9);
gunnerus_surge_speed = gunnerus(:,10);
gunnerus_load_feedback_portside = gunnerus(:,14); % loadpercent of 500 kW
gunnerus_load_feedback_starboard = gunnerus(:,17); % loadpercent of 500 kW
gunnerus_power_portside = gunnerus_load_feedback_portside * 5; % kW
gunnerus_power_starboard = gunnerus_load_feedback_starboard * 5; % kW
wind_speed = smoothdata(gunnerus(:,18)); % m/s
wind_dir = smoothdata(gunnerus(:,19))-180;

vessel_north = vessel(:,3);
vessel_east = vessel(:,4);
vessel_heading = vessel(:,6);
vessel_true_heading = vesselModelObserver(:,3);
vessel_surge_speed = vessel(:,9);

portAzimuth_output_revs = portAzimuth(:,5);
portAzimuth_output_torque = portAzimuth(:,7);

starboardAzimuth_output_revs = starboardAzimuth(:,5);
starboardAzimuth_output_torque = starboardAzimuth(:,7);

% shape of a ship
vessel_shape = vessel_shape(16);

%% NE Plot
h = figure('name', 'North-east plot');

hold on;
plot(vessel_east, vessel_north, 'r'); 
plot(gunnerus_east, gunnerus_north, 'b')
ylabel('North [m]')
xlabel('East [m]')
title('NE plot')
grid on

min_wind_speed = min(wind_speed);
max_wind_speed = max(wind_speed);

q = quiver(-10,-10,1,0,'b'); %plot the arrow at (-10,-10)

xlim([5000, 11500])
ylim([3500, 8500])

for i=10:1000:length(t)
    vessel_rot = vessel_shape*rot_2d(vessel_heading(i))';
    vessel_plot_vec = [vessel_rot(:,1) + vessel_east(i), vessel_rot(:,2) + vessel_north(i)];
    plot(vessel_plot_vec(:,1), vessel_plot_vec(:,2), 'r', 'HandleVisibility', 'off')

    gunnerus_rot = vessel_shape*rot_2d(gunnerus_heading(i))';
    gunnerus_plot_vec = [gunnerus_rot(:,1) + gunnerus_east(i), gunnerus_rot(:,2) + gunnerus_north(i)];
    plot(gunnerus_plot_vec(:,1), gunnerus_plot_vec(:,2), 'b', 'HandleVisibility', 'off')
    
    [base_x, base_y] = normalize_coordinate(gunnerus_east(i), gunnerus_north(i), get(gca, 'Position'), get(gca, 'xlim'), get(gca, 'ylim'));
    [end_x, end_y] = pol2cart(deg2rad(wind_dir(i)), map(wind_speed(i), min_wind_speed, max_wind_speed, 0, 0.05));
    arrow = annotation('arrow', [base_x, base_x + end_x], [base_y, base_y + end_y] );
    configureArrow(arrow, 'blue');
end

legend('Twin', 'Ref', 'Wind direction')

if saveimg
    print(strcat(plotDir, '/NE_plot.eps'), '-depsc')
    saveas(h, strcat(plotDir, '/NE_plot.png')); 
end

%% Speed

h = figure('name', 'Surge speed');

plot(t, vessel_surge_speed, 'r'); hold on;
plot(t, gunnerus_surge_speed, 'b')
legend('Twin', 'Ref')
ylabel('Velocity [m/s]')
xlabel('Time [s]')
title('Surge speed')
grid on

if saveimg
   print(strcat(plotDir, '/Speed_plot.eps'), '-depsc')
   saveas(h, strcat(plotDir, '/Speed_plot.png')); 
end

%% Heading

h = figure('name', 'Ship course');
hold on
plot(t, vessel_true_heading, 'r')
plot(t, gunnerus_true_heading, 'b')
legend({'Twin', 'Ref'}, 'Location', 'northwest')
ylabel('Course [deg]')
xlabel('Time [s]')
title('Ship course')
grid on

if saveimg
    print(strcat(plotDir, '/Heading_plot.eps'), '-depsc')
    saveas(h, strcat(plotDir, '/Heading_plot.png'));
end


%% Heading2

% h = figure('name', 'Heading');
% hold on
% plot(t, vessel_heading, 'r')
% plot(t, gunnerus_heading, 'b')
% legend({'Twin - yaw angle', 'Ref - ship course'}, 'Location', 'northwest')
% ylabel('Heading [deg]')
% xlabel('Time [s]')
% title('Heading')
% grid on
% 
% if saveimg
%     saveas(h, strcat(plotDir, '/Heading_plot.png'));
% end

%% Wind

% h = figure('name', 'Wind');
% 
% hold on
% p = plot(t, wind_speed);
% %legend('wind');
% ylabel('Speed [m/s]')
% xlabel('Time [s]')
% title('Wind')
% grid on
% q = quiver(-10,-10,1,0,'b'); %plot the arrow at (-10,-10)
% xlim([0,2000])
% ylim([3,9])
% for i=1000:750:length(t)-1000
%     [base_x, base_y] = normalize_coordinate(t(i), wind_speed(i), get(gca, 'Position'), get(gca, 'xlim'), get(gca, 'ylim'));
%     [end_x, end_y] = pol2cart(deg2rad(wind_dir(i)), 0.05);
%     arrow = annotation('arrow', [base_x, base_x + end_x], [base_y, base_y + end_y] );
%     configureArrow(arrow, 'blue');
% end
% 
% legend([p q],{'Wind speed', 'Wind direction'});
% 
% if saveimg
%     print(strcat(plotDir, '/Wind_plot.eps'), '-depsc')
%     saveas(h, strcat(plotDir, '/Wind_plot.png'));
% end

%% Power

h = figure('name', 'Azimuth power consumption');

% http://wentec.com/unipower/calculators/power_torque.asp
portAzimuth_power = portAzimuth_output_torque .* portAzimuth_output_revs / 9.5488 / 1000; % kW
starboardAzimuth_power = starboardAzimuth_output_torque .* starboardAzimuth_output_revs / 9.5488 / 1000; % kW

gunnerus_power = (gunnerus_power_portside + gunnerus_power_starboard);
twin_power = (portAzimuth_power + starboardAzimuth_power);

hold on
plot(t, twin_power, 'r')
plot(t, gunnerus_power, 'b');
l = legend('Twin', 'Ref');
set(l,'Interpreter', 'none')
ylabel('Power [kW]')
xlabel('Time [s]')
title('Power consumption - Azimuths')
grid on

if saveimg
    print(strcat(plotDir, '/Power_plot.eps'), '-depsc')
    saveas(h, strcat(plotDir, '/Power_plot.png'));
end
