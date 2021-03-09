%% Header
% Written by Abraham Asfaw
% asfaw@princeton.edu
% Inspired by regular plot version here:
% http://www.mathworks.com/matlabcentral/fileexchange/10656-data-space-to-figure-units-conversion

%% Function
% x_point = x value that you want to be normalized
% y_point = y value that corresponds to the x_point
% axes = axes of the current plot, usually found using get(gca, 'Position')
% xlims_ = x limits of the current plot, usually found using get(gca, 'xlim')
% ylims_ = y limits of the current plot, usually found using get(gca, 'ylim')

function [nx, ny] = normalize_coordinate(x_point_, y_point_, axes, xlims_, ylims_)

    x_point = x_point_;
    y_point = y_point_;
    xlims = xlims_;
    ylims = ylims_;
    
    nx = ((x_point-xlims(1))/(xlims(2) - xlims(1)))*axes(3);
    ny = ((y_point-ylims(1))/(ylims(2) - ylims(1)))*axes(4);
    nx = axes(1) + nx;
    ny = axes(2) + ny;
end