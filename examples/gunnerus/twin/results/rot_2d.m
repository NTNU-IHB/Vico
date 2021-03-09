function R = rot_2d(theta)
% rotation of local vessel coordinates (shape) to xy plot
    theta = theta - 90;
    R = [-sind(theta), cosd(theta);
        -cosd(theta), -sind(theta)];
end
